package edu.uw.tcss450.team3.tiktalk.ui.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.team3.tiktalk.databinding.FragmentWeatherFirstBinding;
import edu.uw.tcss450.team3.tiktalk.model.LocationViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFirstFragment extends Fragment {

    private LocationViewModel mModel;

    private String coorWeatherURL = "https://tiktalk-app-web-service.herokuapp.com/weather/lat-lon";
    private String zipcodeWeatherURL= "https://tiktalk-app-web-service.herokuapp.com/weather/zipcode";

    // Hard coded for the location --> UWT
    private static final String HARD_CODED_LATITUDE = "47.2454";
    private static final String HARD_CODED_LONGITUDE = "-122.4385";
    private static final String HARD_CODED_ZIPCODE = "98402";

    private WeatherViewModel mWeatherViewModel;
    private FragmentWeatherFirstBinding mBinding;
    private WeatherFirstFragment weatherFirstFragment;


    public WeatherFirstFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mWeatherViewModel = provider.get(WeatherViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentWeatherFirstBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentWeatherFirstBinding binding = FragmentWeatherFirstBinding.bind(getView());
        mModel = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        mModel.addLocationObserver(getViewLifecycleOwner(), location ->
                binding.idTVCityName.setText(location.toString()));
    }


//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        mWeatherViewModel.addResponseObserver(getViewLifecycleOwner(), result ->
//            mBinding.textResponseOutput.setText(result.toString()));
//        mBinding.textResponseOutput.setOnClickListener(weatherFirstFragment -> mWeatherViewModel.connectGet());
//        mBinding.textResponseOutput.setOnClickListener(WeatherFirstFragment -> mWeatherViewModel.connectPost());
//    }
}