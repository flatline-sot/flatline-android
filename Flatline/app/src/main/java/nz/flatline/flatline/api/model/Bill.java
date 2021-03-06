package nz.flatline.flatline.api.model;


import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Bill {
    public final int id;

    @SerializedName("start")
    public final Date startDate;

    @SerializedName("end")
    public final Date endDate;

    public final double cost;

    @SerializedName("cost_per_user")
    public final double costPerUser;

    public final Flat flat;

    public Bill(int id, Date startDate, Date endDate, double cost, double costPerUser, Flat flat) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cost = cost;
        this.costPerUser = costPerUser;
        this.flat = flat;
    }

    public String getReadableEndDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        return sdf.format(endDate);
    }

}
