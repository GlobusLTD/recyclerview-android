package com.globusltd.recyclerview.sample.lifecycle;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.datasource.ListDatasource;
import com.globusltd.recyclerview.sample.data.Person;
import com.globusltd.recyclerview.sample.data.PersonGenerator;

public class LifecycleExampleViewModel extends ViewModel {

    @NonNull
    private final ListDatasource<Person> mDatasource;

    public LifecycleExampleViewModel() {
        mDatasource = new ListDatasource<>();

        final PersonGenerator personGenerator = new PersonGenerator();
        for (int i = 0; i < 50; i++) {
            final Person person = personGenerator.generate();
            mDatasource.add(person);
        }
    }

    @NonNull
    Datasource<Person> getDatasource() {
        return mDatasource;
    }

}
