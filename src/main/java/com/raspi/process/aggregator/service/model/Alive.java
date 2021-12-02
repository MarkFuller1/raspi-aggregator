package com.raspi.process.aggregator.service.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raspi.process.aggregator.Util.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Alive {
    String alive;

    @Override
    public String toString(){
        return Utils.toJson(this);
    }
}
