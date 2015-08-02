package nz.flatline.flatline.api;


import android.util.Log;

import java.util.List;

import nz.flatline.flatline.api.model.Bill;
import nz.flatline.flatline.api.model.BillService;
import nz.flatline.flatline.api.model.BillUI;
import nz.flatline.flatline.api.model.FlatlineRestClient;
import rx.Subscriber;
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

        flatlineRestClient.getFlatLineAPI().bill(1).observeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Bill>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Flatline", e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<Bill> bills) {
                        billUI.onBillsReceived(bills);
                    }
                });

    }
}
