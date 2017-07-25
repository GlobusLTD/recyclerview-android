package com.globusltd.recyclerview.sample.choicemode;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.globusltd.recyclerview.datasource.Datasource;
import com.globusltd.recyclerview.datasource.ListDatasource;
import com.globusltd.recyclerview.sample.data.Person;
import com.globusltd.recyclerview.sample.data.PersonGenerator;

public class PersonsViewModel extends ViewModel {
    
    @NonNull
    private final PersonGenerator mPersonGenerator;
    
    @NonNull
    private final ListDatasource<Person> mDatasource;
    
    public PersonsViewModel() {
        mPersonGenerator = new PersonGenerator();
        mDatasource = new ListDatasource<>();
        for (int i = 0; i < 50; i++) {
            mDatasource.add(mPersonGenerator.generate());
        }
    }
    
    @NonNull
    Datasource<Person> getDatasource() {
        return mDatasource;
    }
    
    void deleteById(final long itemId) {
        final int size = mDatasource.size();
        for (int index = size - 1; index >= 0; index--) {
            final Person person = mDatasource.get(index);
            if (person.getId() == itemId) {
                mDatasource.remove(index);
                break;
            }
        }
    }
    
}