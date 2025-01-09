package model;

public class AttachmentsPerBoard {
    private String status;
    private int disableAt;
    private int warnAt;

    public AttachmentsPerBoard(String status, int disableAt, int warnAt) {
        this.status = status;
        this.disableAt = disableAt;
        this.warnAt = warnAt;
    }
}
