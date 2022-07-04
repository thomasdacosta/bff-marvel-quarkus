package br.com.marvel.utils;

import br.com.marvel.resource.dto.Pagination;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.Response;

public class PaginationUtils {

    public static Response pagination(Pagination pagination) {
        return Response.ok(pagination.getData())
                    .header("offset", pagination.getOffset().toString())
                    .header("limit", pagination.getLimit().toString())
                    .header("total", pagination.getTotal().toString())
                    .header("count", pagination.getCount().toString())
                    .header("fileName", StringUtils.isEmpty(pagination.getFileName()) ? "" : pagination.getFileName())
                .build();
    }

}