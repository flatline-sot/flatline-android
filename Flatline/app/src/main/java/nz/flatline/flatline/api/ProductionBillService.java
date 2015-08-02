package nz.flatline.flatline.api;


import java.util.ArrayList;
import java.util.List;

import nz.flatline.flatline.api.model.Bill;
import nz.flatline.flatline.api.model.BillService;
import nz.flatline.flatline.api.model.BillUI;
import nz.flatline.flatline.api.model.FlatlineRestClient;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ProductionBillService implements BillService {

    private BillUI billUI;
    private FlatlineRestClient flatlineRestClient;

    public ProductionBillService(BillUI billUI, String API_URL) {
        this.billUI = billUI;
        flatlineRestClient = new FlatlineRestClient(API_URL);
    }

    @Override
    public void requestBillData() {

        flatlineRestClient.getFlatLineAPI().bill(1).subscribeOn(Schedulers.io())
                .subscribe(new Action1<List<Bill>>() {
                               @Override
                               public void call(List<Bill> bills) {
                                   billUI.onBillsReceived(bills);
                               }
                           });

    }
}
