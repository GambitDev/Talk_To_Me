package com.gambitdev.talktome.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.gambitdev.talktome.Interfaces.OnProfileImgOptions;
import com.gambitdev.talktome.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

public class EditStatusBottomSheet extends BottomSheetDialogFragment {

    private OnProfileImgOptions listener;

    public void setListener(OnProfileImgOptions listener) {
        this.listener = listener;
    }

    public static EditStatusBottomSheet newInstance() {
        return new EditStatusBottomSheet();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_status_bottom_sheet, container , false);

        LinearLayout root = view.findViewById(R.id.root_view);
        BottomSheetBehavior.from(root).setState(BottomSheetBehavior.STATE_EXPANDED);

        Button editBtn = view.findViewById(R.id.edit_btn);
        TextInputLayout inputLayout = view.findViewById(R.id.status_input);
        editBtn.setOnClickListener(v -> {
            String status = getStatus(inputLayout);
            if (status != null) updateStatus(status);
        });

        return view;
    }

    @Override
    public int getTheme() {
        return R.style.CustomBottomSheetDialog;
    }

    private String getStatus(TextInputLayout inputLayout) {
        String status;
        if (inputLayout.getEditText() != null) {
            status = inputLayout.getEditText().getText().toString();
            if (!status.isEmpty() && status.length() <= 30)
                return status;
            else if (status.isEmpty())
                inputLayout.setError(getResources().getString(R.string.empty_status_error));
            else
                inputLayout.setError(getResources().getString(R.string.long_status_error));
        }
        return null;
    }

    private void updateStatus(String status) {
        listener.onEditStatusBtnClicked(status);
        dismiss();
    }
}
