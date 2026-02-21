package org.banew.hdh.fxapp.implementations.services;

import javafx.application.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class AsyncRunner {

    private final ExecutorService executorService;

    public <T> void future(Callable<T> callable, Consumer<T> success, Consumer<Exception> failure) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executorService).thenAccept(t -> {
            Platform.runLater(() -> {
                success.accept(t);
            });
        }).exceptionally(e -> {
            // Витягуємо реальну причину (cause) з обгортки
            Throwable cause = (e.getCause() != null) ? e.getCause() : e;
            Platform.runLater(() -> {
                failure.accept((Exception) cause);
            });
            return null; // exceptionally має щось повернути
        });
    }
}
