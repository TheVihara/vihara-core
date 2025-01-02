package me.vihara.core.impl;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Data
public final class ImplModel implements Comparable<ImplModel> {

    String implName;
    ImplPriority priority;

    @Override
    public int compareTo(final @NotNull ImplModel o) {
        return priority.compareTo(o.priority);
    }
}
