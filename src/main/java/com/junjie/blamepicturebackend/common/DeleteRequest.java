package com.junjie.blamepicturebackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用删除请求包装类
 */

@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}

