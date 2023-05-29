package de.malte.event;

import de.malte.event.annotations.Subscribe;
import de.malte.event.enums.ErrorHandling;
import de.malte.event.events.EventCancellable;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class EventBus {
    private final Map<Class<?>, Set<MethodInfo>> listenerMap = new HashMap<>();
    private final ErrorHandling errorHandling;

    public EventBus() {
        this(ErrorHandling.LOG);
    }

    public EventBus(ErrorHandling errorHandling) {
        this.errorHandling = errorHandling;
    }

    public void register(Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            Class<?>[] params = method.getParameterTypes();
            if (params.length != 1 || !method.isAnnotationPresent(Subscribe.class))
                return;

            if (!method.isAccessible())
                method.setAccessible(true);

            Class<?> clazz = params[0];
            Subscribe annotation = method.getAnnotation(Subscribe.class);
            MethodInfo info = new MethodInfo(listener, method, annotation.value(), annotation.ignoreCancelled());
            listenerMap.computeIfAbsent(clazz, set -> new HashSet<>()).add(info);
        }
    }

    public void unregister(Object listener) {
        listenerMap.forEach((k, v) -> v.removeIf(info -> info.getListener().equals(listener)));
    }

    public void call(Object event) {
        final Set<MethodInfo> wrappers = listenerMap.get(event.getClass());
        if (wrappers == null)
            return;
        final List<MethodInfo> sortedWrappers = wrappers.stream().sorted(Comparator.comparingInt(info -> -info.getPriority().ordinal())).collect(Collectors.toList());
        sortedWrappers.forEach(info -> {
            if (event instanceof EventCancellable && ((EventCancellable) event).isCancelled() && info.isIgnoreCancelled())
                return;
            try {
                info.getMethod().invoke(info.getListener(), event);
            } catch (InvocationTargetException e) {
                switch (errorHandling) {
                    case LOG:
                        e.getCause().printStackTrace();
                        break;
                    case THROW:
                        throw new RuntimeException(e.getCause());
                }
            } catch (IllegalAccessException e) {
                switch (errorHandling) {
                    case LOG:
                        e.printStackTrace();
                        break;
                    case THROW:
                        throw new RuntimeException(e);
                }
            }
        });
    }
}
