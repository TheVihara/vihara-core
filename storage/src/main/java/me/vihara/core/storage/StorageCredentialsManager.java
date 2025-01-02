package me.vihara.core.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Getter
public abstract class StorageCredentialsManager {
    final AtomicInteger usages;
}
