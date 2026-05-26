package com.bloomfinance.common.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

/**
 * 分页响应封装
 *
 * @param <T> 记录数据类型
 * @author BloomFinance
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "分页响应封装")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResult<T> extends Result<List<T>> {

    private static final long serialVersionUID = 1L;

    /**
     * 记录列表
     */
    @Schema(description = "记录列表")
    private List<T> records;

    /**
     * 总记录数
     */
    @Schema(description = "总记录数", example = "100")
    private long total;

    /**
     * 每页大小
     */
    @Schema(description = "每页大小", example = "20")
    private long size;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1")
    private long current;

    /**
     * 总页数
     */
    @Schema(description = "总页数", example = "5")
    private long pages;

    public PageResult() {
        this.timestamp = System.currentTimeMillis();
        this.traceId = UUID.randomUUID().toString().replace("-", "");
        this.code = 200;
        this.message = "success";
    }

    /**
     * 从MyBatis-Plus分页对象转换
     *
     * @param page  MyBatis-Plus分页对象
     * @param clazz 目标类型Class
     * @param <T>   目标类型
     * @param <P>   分页对象类型
     * @return 分页响应
     */
    public static <T, P extends IPage<T>> PageResult<T> of(IPage<T> page, Class<T> clazz) {
        PageResult<T> result = new PageResult<>();
        result.setRecords(page.getRecords());
        result.setTotal(page.getTotal());
        result.setSize(page.getSize());
        result.setCurrent(page.getCurrent());
        result.setPages(page.getPages());
        return result;
    }

    /**
     * 构建分页结果
     *
     * @param records 记录列表
     * @param total   总记录数
     * @param current 当前页码
     * @param size    每页大小
     * @param <T>     数据类型
     * @return PageResult
     */
    public static <T> PageResult<T> of(List<T> records, long total, int current, int size) {
        PageResult<T> result = new PageResult<>();
        result.setRecords(records);
        result.setTotal(total);
        result.setSize(size);
        result.setCurrent(current);
        result.setPages((int) Math.ceil((double) total / size));
        return result;
    }

    /**
     * 构建分页结果（无records）
     *
     * @param total   总记录数
     * @param current 当前页码
     * @param size    每页大小
     * @param <T>     数据类型
     * @return PageResult
     */
    public static <T> PageResult<T> of(long total, int current, int size) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(total);
        result.setSize(size);
        result.setCurrent(current);
        result.setPages((int) Math.ceil((double) total / size));
        return result;
    }
}