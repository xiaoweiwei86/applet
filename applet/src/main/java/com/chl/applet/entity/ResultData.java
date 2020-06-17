package com.chl.applet.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultData<T> {
    private Integer status;
    private String msg;
    private T data;

    public ResultData(Integer status,String msg){
        this.status=status;
        this.msg=msg;
    }

}
