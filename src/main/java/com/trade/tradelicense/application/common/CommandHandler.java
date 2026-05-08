package com.trade.tradelicense.application.common;

public interface CommandHandler<C, R> {
    R handle(C command);
}
