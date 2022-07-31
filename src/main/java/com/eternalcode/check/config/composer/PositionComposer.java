package com.eternalcode.check.config.composer;

import com.eternalcode.check.shared.position.Position;
import panda.std.Result;

public class PositionComposer implements SimpleComposer<Position> {

    @Override
    public Result<Position, Exception> deserialize(String source) {
        return Result.attempt(IllegalArgumentException.class, () -> Position.parse(source));
    }

    @Override
    public Result<String, Exception> serialize(Position entity) {
        return Result.ok(entity.toString());
    }

}
