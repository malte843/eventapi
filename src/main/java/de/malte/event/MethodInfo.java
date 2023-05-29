package de.malte.event;

import de.malte.event.enums.Priority;
import lombok.Data;

import java.lang.reflect.Method;

@Data
public class MethodInfo {
    private final Object listener;
    private final Method method;
    private final Priority priority;
    private final boolean ignoreCancelled;
}
