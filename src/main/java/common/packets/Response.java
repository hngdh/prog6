package common.packets;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
    private List<String> notice;
    private List<String> result;

    public Response(List<String> notice, List<String> result) {
        this.notice = notice;
        this.result = result;
    }

    public Response() {
    }

    public void addNotice(String notice) {
        this.notice.add(notice);
    }

    public List<String> getNotice() {
        return notice;
    }

    public List<String> getResult() {
        return result;
    }
}
