package com.eternalcode.check.configuration.composer;

import panda.std.Result;

public class StringComposer implements SimpleComposer<String>{

    @Override
    public Result<String, Exception> deserialize(String source) {
        if (!source.startsWith("\"") || !source.endsWith("\"") || source.length() < 2) {
            return Result.error(new IllegalStateException("Brakuje \" w confingu! Sprawdz swoja konfiguracje na: https://tiny.pl/9n1kv"));
        }

        return Result.ok(source.substring(1, source.length() - 1));

    }

    @Override
    public Result<String, Exception> serialize(String entity) {
        return Result.ok("\"" + entity + "\"");
    }
}
