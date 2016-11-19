package com.littlehouse_design.qpstoday;

import java.util.ArrayList;

/**
 * Created by johnkonderla on 11/12/16.
 */

public class ButtonChanger {

    private int status;
    private String[] words = {"Working", "Unstable", "Broken"};
    private int color;
    private int image;
    private int type;
    private String preamble = " are currently ";
    private String[] typeWords = {"Binary Syncs", "Data Syncs", "Orders", "Reports"};

    public ButtonChanger(int status, int color, int type) {
        this.status=status;
        this.color=color;
        this.type=type;
    }
    public ButtonChanger(int type) {
        this.type=type;
        status = 0;
        color = R.color.green;
    }
    public void changeStatus() {
        if (type == 0) {
            if (status == 0) {
                this.status = 1;
                this.image = R.drawable.binary_sync_yellow;
            } else if (status == 1) {
                this.status = 2;
                this.image = R.drawable.binary_sync_red;
            } else {
                this.status = 0;
                this.image = R.drawable.binary_sync_green;
            }
        } else if(type == 1) {
            if (status == 0) {
                this.status = 1;
                this.image = R.drawable.data_sync_yellow;
            } else if (status == 1) {
                this.status = 2;
                this.image = R.drawable.data_sync_red;
            } else {
                this.status = 0;
                this.image = R.drawable.data_sync_green;
            }
        }
        else if(type ==2) {
            if (status == 0) {
                this.status = 1;
                this.image = R.drawable.order_yellow;
            } else if (status == 1) {
                this.status = 2;
                this.image = R.drawable.order_red;
            } else {
                this.status = 0;
                this.image = R.drawable.order_green;
            }
        }
        else {

            if (status == 0) {
                this.status = 1;
                this.image = R.drawable.report_yellow;
            } else if (status == 1) {
                this.status = 2;
                this.image = R.drawable.report_red;
            } else {
                this.status = 0;
                this.image = R.drawable.report_green;
            }

        }
    }
    public int getStatus() {
        return status;
    }
    public String getTextStatus() {

        return typeWords[type] + preamble + words[status];
    }
    public int getImage() {
        return image;
    }

}
