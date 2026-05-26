package com.bloomfinance.bookkeeping.service.impl;

import com.bloomfinance.bookkeeping.domain.entity.CategoryEntity;
import com.bloomfinance.bookkeeping.domain.enums.TransactionTypeEnum;
import com.bloomfinance.bookkeeping.domain.vo.CategoryVO;
import com.bloomfinance.bookkeeping.repository.CategoryRepository;
import com.bloomfinance.bookkeeping.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分类服务实现类
 *
 * @author BloomFinance
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private static final String CACHE_NAME = "category";

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @Cacheable(value = CACHE_NAME, key = "'list'", unless = "#result.isEmpty()")
    public List<CategoryVO> listCategories() {
        log.info("查询所有分类列表");

        List<CategoryEntity> entities = categoryRepository.findAll();
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @Cacheable(value = CACHE_NAME, key = "'tree'", unless = "#result.isEmpty()")
    public List<CategoryVO> getCategoryTree() {
        log.info("获取分类树形结构");

        List<CategoryEntity> entities = categoryRepository.findAll();
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }

        // 按父ID分组
        Map<Long, List<CategoryEntity>> groupByParentId = entities.stream()
                .collect(Collectors.groupingBy(CategoryEntity::getParentId));

        // 构建树形结构
        return buildTree(groupByParentId, 0L);
    }

    /**
     * 构建树形结构
     *
     * @param groupByParentId 按父ID分组的分类
     * @param parentId        父ID
     * @return 子分类列表
     */
    private List<CategoryVO> buildTree(Map<Long, List<CategoryEntity>> groupByParentId, Long parentId) {
        List<CategoryEntity> children = groupByParentId.get(parentId);
        if (CollectionUtils.isEmpty(children)) {
            return Collections.emptyList();
        }

        return children.stream()
                .map(entity -> {
                    CategoryVO vo = convertToVO(entity);
                    vo.setChildren(buildTree(groupByParentId, entity.getId()));
                    return vo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 实体转换为VO
     *
     * @param entity 分类实体
     * @return 分类VO
     */
    private CategoryVO convertToVO(CategoryEntity entity) {
        CategoryVO vo = new CategoryVO();
        BeanUtils.copyProperties(entity, vo);

        // 设置类型名称
        TransactionTypeEnum typeEnum = TransactionTypeEnum.fromCode(entity.getType());
        if (typeEnum != null) {
            vo.setTypeName(typeEnum.getDescription());
        }

        return vo;
    }
}