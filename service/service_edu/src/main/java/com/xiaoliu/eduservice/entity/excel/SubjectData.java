package com.xiaoliu.eduservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SubjectData {

    //第一列
    @ExcelProperty(index = 0)
    private String oneSubjectName;

    //第二列
    @ExcelProperty(index = 1)
    private String twoSubjectName;
}
