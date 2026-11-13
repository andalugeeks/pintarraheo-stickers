/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package es.andaluh.pintarraheoStickers;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.DialogFragment;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        applySystemBarInsets();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Light background behind the system bars: use dark icons.
        WindowInsetsControllerCompat controller =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        controller.setAppearanceLightStatusBars(true);
        controller.setAppearanceLightNavigationBars(true);
    }

    // Since targetSdk 35 the app is drawn edge-to-edge: keep the action bar and
    // content out from under the status and navigation bars.
    private void applySystemBarInsets() {
        View root = findViewById(androidx.appcompat.R.id.decor_content_parent);
        if (root == null) {
            root = findViewById(android.R.id.content);
        }
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        ViewCompat.requestApplyInsets(root);

        // AppCompat no longer offsets the content below the action bar under
        // edge-to-edge, so reserve its height explicitly.
        View actionBar = findViewById(androidx.appcompat.R.id.action_bar_container);
        View content = findViewById(android.R.id.content);
        if (actionBar != null && content != null) {
            actionBar.addOnLayoutChangeListener((v, l, t, r, b, ol, ot, or, ob) -> {
                int height = v.getVisibility() == View.VISIBLE ? b - t : 0;
                if (content.getPaddingTop() != height) {
                    content.setPadding(content.getPaddingLeft(), height,
                            content.getPaddingRight(), content.getPaddingBottom());
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static final class MessageDialogFragment extends DialogFragment {
        private static final String ARG_TITLE_ID = "title_id";
        private static final String ARG_MESSAGE = "message";

        static DialogFragment newInstance(@StringRes int titleId, String message) {
            DialogFragment fragment = new MessageDialogFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_TITLE_ID, titleId);
            arguments.putString(ARG_MESSAGE, message);
            fragment.setArguments(arguments);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            @StringRes final int title = getArguments().getInt(ARG_TITLE_ID);
            String message = getArguments().getString(ARG_MESSAGE);

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                    .setMessage(message)
                    .setCancelable(true)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> dismiss());

            if (title != 0) {
                dialogBuilder.setTitle(title);
            }
            return dialogBuilder.create();
        }
    }
}
