package org.learn.dto.response;


import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskResponnse {
    private String result;

    public synchronized String getResult() {
        return result;
    }

    public synchronized void setResult(String result) {
        this.result = result;
    }
}

