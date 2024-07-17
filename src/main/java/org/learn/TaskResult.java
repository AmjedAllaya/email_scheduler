package org.learn;


import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskResult {
    private String result;

    public synchronized String getResult() {
        return result;
    }

    public synchronized void setResult(String result) {
        this.result = result;
    }
}

