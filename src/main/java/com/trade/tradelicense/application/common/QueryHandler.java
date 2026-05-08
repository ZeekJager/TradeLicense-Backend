package com.trade.tradelicense.application.common;

public interface QueryHandler<Q, R> {
    R handle(Q query);
}
