package com.example.onlinejudge.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessRet {
    @Override
    public String toString() {
        return "ProcessRet{" +
                "exitStatus=" + exitStatus +
                ", output=" + output +
                '}';
    }

    private int exitStatus;
    private String output;

}
