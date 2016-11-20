package com.littlehouse_design.qpstoday;

import java.util.ArrayList;

/**
 * Created by johnkonderla on 11/12/16.
 */

public class ButtonChanger {

    private int status;
    private String[] words = {"Working", "Unstable", "Broken"};
    private int image;
    private int type;
    private String preamble = " are currently ";
    private String[] typeWords = {"Binary Syncs", "Data Syncs", "Orders", "Reports"};

    /**
     * Constructor of the class.
     * @param status Sets the status, either Working, Unstable, or Broken.
     * @param type Sets the type, either Binary Sync, Data Sync, Orders, or Reports.
     */

    public ButtonChanger(int status, int type) {
        this.status=status;
        this.type=type;
    }
    /**
     * Constructor of the class. Default is working! Maybe too high of hopes...
     * @param type Sets the type, either Binary Sync, Data Sync, Orders, or Reports.
     *
     */
    public ButtonChanger(int type) {
        this.type=type;
        status = 0;
    }

    /**
     * Advances the status of an object from this class to the next status.
     */
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

    /**
     * gets the status!
     * @return int that has the most up to date status.
     */
    public int getStatus() {
        return status;
    }

    /**
     * what to display in the out going email or on screen.
     * @return
     */
    public String getTextStatus() {

        return typeWords[type] + preamble + words[status];
    }

    /**
     * gives back the current type's status image
     * @return drawable resource addresss
     */
    public int getImage() {
        return image;
    }

}
