package com.example.mygrocerystore.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.mygrocerystore.R;
import com.example.mygrocerystore.adapters.HomeCategoryAdapter;
import com.example.mygrocerystore.adapters.PopularAdapters;
import com.example.mygrocerystore.adapters.RecommendedAdapter;
import com.example.mygrocerystore.adapters.ViewAllAdapter;
import com.example.mygrocerystore.models.HomeCategoryModel;
import com.example.mygrocerystore.models.PopularModel;
import com.example.mygrocerystore.models.ViewAllModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    ScrollView scrollView;
    private ImageView progressBar, progressBar1;
    private LottieAnimationView progressBar2;
    RecyclerView popularRec, homecatRec, recommendedRec, viewallRec;
    FirebaseFirestore db;

    //Popular Item.....
    List<PopularModel> popularModelList;
    PopularAdapters popularAdapters;

    //Home Category........
    List<HomeCategoryModel> categoryModelList;
    HomeCategoryAdapter categoryAdapter;

    //Recommended Item......
    List<ViewAllModel> recommendedModelList;
    RecommendedAdapter recommendedAdapter;

    //Search View
    SearchView searchView;
    private List<ViewAllModel> viewAllModelList;
    private ViewAllAdapter viewAllAdapter;

    @SuppressLint("NotifyDataSetChanged")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseFirestore.getInstance();

        popularRec = root.findViewById(R.id.pop_rec);
        homecatRec = root.findViewById(R.id.explore_rec);
        recommendedRec = root.findViewById(R.id.recommended_rec);
//        viewallRec=root.findViewById(R.id.view_all_rec);
        scrollView = root.findViewById(R.id.scroll_view);

        //ProgresBar ID Fetch
        progressBar = root.findViewById(R.id.progress_bar);
        progressBar1 = root.findViewById(R.id.progress_bar1);
        progressBar2 = root.findViewById(R.id.progress_bar2);
        //Gone ProgresBar
        progressBar.setVisibility(View.VISIBLE);
        progressBar1.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);

        scrollView.setVisibility(View.GONE);

        //popular Item...
        popularRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        popularModelList = new ArrayList<>();
        popularAdapters = new PopularAdapters(getActivity(), popularModelList);
        popularRec.setAdapter(popularAdapters);

        db.collection("PopularProducts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            PopularModel popularModel = document.toObject(PopularModel.class);
                            popularModelList.add(popularModel);
                            popularAdapters.notifyDataSetChanged();

                            progressBar.setVisibility(View.GONE);
                            progressBar1.setVisibility(View.GONE);
                            progressBar2.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });

        //Home Category....
        homecatRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new HomeCategoryAdapter(getActivity(), categoryModelList);
        homecatRec.setAdapter(categoryAdapter);

        db.collection("HomeCategory")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            HomeCategoryModel categoryModel = document.toObject(HomeCategoryModel.class);
                            categoryModelList.add(categoryModel);
                            categoryAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });

        //Recommended Item...
        recommendedRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recommendedModelList = new ArrayList<>();
        recommendedAdapter = new RecommendedAdapter(getActivity(), recommendedModelList);
        recommendedRec.setAdapter(recommendedAdapter);

     /*   db.collection("Recommended")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RecommendedModel recommendedModel = document.toObject(RecommendedModel.class);
                                recommendedModelList.add(recommendedModel);
                                recommendedAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
*/
        //Search Productg
        viewAllModelList = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(getContext(), viewAllModelList);

        db.collection("AllProducts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            String document_id = document.getId();
                            ViewAllModel viewAllModel = document.toObject(ViewAllModel.class);
                            assert viewAllModel != null;
                            viewAllModel.setDocumentId(document_id);
                            viewAllModelList.add(viewAllModel);
                            recommendedModelList.add(viewAllModel);
                            if (!recommendedModelList.isEmpty()) {
                                Collections.shuffle(recommendedModelList);
                            }
                            viewAllAdapter.notifyDataSetChanged();
                            recommendedAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });

        RecyclerView recyclerViewSearch = (RecyclerView) root.findViewById(R.id.search_prod);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));

//        searchView=(SearchView) root.findViewById(R.id.seachView);
//        searchView.clearFocus();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String newText) {
//                filterList(newText);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filterList(newText);
//                return true;
//            }
//        });

        //imageSlider implementation
        ImageSlider imageSlider;
        imageSlider = root.findViewById(R.id.imageSliderHome);
        ArrayList<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel("https://www.zopmart.in/wp-content/uploads/2020/11/grocery-shopping-discount-banner.jpg", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://thumbs.dreamstime.com/z/grocery-food-store-shopping-basket-promotional-sale-banner-grocery-food-store-shopping-basket-promotional-sale-banner-196209213.jpg", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://d39vol41m7feya.cloudfront.net/1629253160-banner1.jfif", ScaleTypes.FIT));
        imageList.add(new SlideModel("https://dealroup.com/wp-content/uploads/2020/05/Grocery-Offers-1024x536.jpg", ScaleTypes.FIT));

        imageSlider.setImageList(imageList, ScaleTypes.FIT);

        return root;
    }

//    private void filterList(String text) {
//        if(text.isEmpty())
//        {
//            recyclerViewSearch.setVisibility(View.GONE);
//        }
//        else
//        {
//            recyclerViewSearch.setVisibility(View.VISIBLE);
//            List<ViewAllModel> filteredList=new ArrayList<>();
//            for (ViewAllModel viewAllModel:viewAllModelList)
//            {
//                if(viewAllModel.getName().toLowerCase().contains(text.toLowerCase()))
//                {
//                    filteredList.add(viewAllModel);
//                }
//                recyclerViewSearch.setAdapter(viewAllAdapter);
//            }
//            if(filteredList.isEmpty())
//            {
//                viewAllModelList.clear();
//                Toast.makeText(getContext(), "No Data Found.", Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
//                viewAllAdapter.setFilteredList(filteredList);
//            }
//        }
//
//    }
}