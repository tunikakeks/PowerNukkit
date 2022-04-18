package cn.nukkit.form.window;

import cn.nukkit.form.handler.FormHandler;
import cn.nukkit.form.response.FormResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FormWindow {

    private static final Gson GSON = new Gson();

    protected transient List<FormHandler> handlers = new ArrayList<>();

    protected transient Map<String, Object> additionalData = new HashMap<>();
    
    protected boolean closed = false;

    public String getJSONData() {
        return FormWindow.GSON.toJson(this);
    }

    public abstract void setResponse(String data);

    public abstract FormResponse getResponse();

    public boolean wasClosed() {
        return closed;
    }

    public List<FormHandler> getHandlers() {
        return handlers;
    }

    public void addHandler(FormHandler handler) {
        this.handlers.add(handler);
    }

    public void removeHandler(FormHandler handler) {
        this.handlers.remove(handler);
    }

    public Object getAdditionalData(String key) {
        return additionalData.get(key);
    }

    public void addAdditionalData(String key, Object value) {
        this.additionalData.put(key, value);
    }

    public void removeAdditionalData(String key) {
        this.additionalData.remove(key);
    }
}
