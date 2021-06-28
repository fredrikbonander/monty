package com.trustly.monty.store;

import com.trustly.monty.dto.Box;
import com.trustly.monty.dto.BoxContent;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BoxDao {
    BoxContent content;

    public Box toDto() {
        var box = new Box();
        box.setContent(this.getContent());

        return box;
    }
}
