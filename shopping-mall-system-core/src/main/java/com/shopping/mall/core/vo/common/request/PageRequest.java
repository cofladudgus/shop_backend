package com.shopping.mall.core.vo.common.request;

import com.google.gson.GsonBuilder;
import com.shopping.mall.core.adapter.LocalDateSerializer;
import com.shopping.mall.core.adapter.LocalDateTimeSerializer;
import com.shopping.mall.core.adapter.LocalTimeSerializer;
import com.shopping.mall.core.enums.SortTypeCode;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;

/**
 * <pre>
 * Description : Paging 데이터 Request
 * Date : 2023/08/03 10:00 AM
 * Company :
 * Author : jypark
 * </pre>
 */
@Slf4j
@Getter
@Setter
@Schema(name = "PageRequest", description = "Paging Request")
@ParameterObject
public class PageRequest<T> {

    /**
     * filter Entity
     */
    @Hidden
    protected T filter;

    /**
     * 페이지당 ROW 수
     */
    @Schema(description = "페이지당 row 수", required = true)
    @Parameter(description = "페이지당 row 수")
    protected Integer pageRowCount;

    /**
     * 현재 페이지
     */
    @Schema(description = "현재 페이지", required = true)
    @Parameter(description = "현재 페이지")
    protected Integer curPage;

    /**
     * 검색 시작일자(YYYY-MM-DD)
     */
    @Schema(description = "검색 시작일자")
    @Parameter(description = "검색 시작일자")
    protected String startDate = LocalDate.now().minusMonths(1L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    /**
     * 검색 종료일자(YYYY-MM-DD)
     */
    @Schema(description = "검색 종료일자")
    @Parameter(description = "검색 종료일자")
    protected String endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    /**
     * 정렬 컬럼명
     */
    @Schema(description = "정렬 컬럼명")
    @Parameter(description = "정렬 컬럼명")
    protected String sortColumn;

    /**
     * 정렬 방법
     */
    @Schema(description = "정렬 방법", defaultValue = "DESC")
    @Parameter(description = "정렬 방법")
    protected SortTypeCode sortType;

    public Integer getStartIndex() {
        return pageRowCount * (curPage.intValue() - 1);
    }

    public PageRequest() {
        this.curPage = 1;
        this.pageRowCount = 10;
        this.sortColumn = "id";
        this.sortType = SortTypeCode.DESC;
        this.filter = null;
    }

    public PageRequest(int curPage, int pageRowCount) {
        this.curPage = curPage;
        this.pageRowCount = pageRowCount;
        this.filter = null;
    }

    @Override
    public String toString() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());

        return gsonBuilder.setPrettyPrinting().create().toJson(this);
    }
}
