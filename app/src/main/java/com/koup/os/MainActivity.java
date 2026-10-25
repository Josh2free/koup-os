package com.koup.os;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private final int BG = Color.rgb(8, 9, 11);
    private final int PANEL = Color.rgb(19, 21, 25);
    private final int WHITE = Color.rgb(245, 246, 248);
    private final int MUTED = Color.rgb(151, 157, 166);
    private final int RED = Color.rgb(227, 74, 74);
    private TextView clockView;
    private TextView dateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
        buildScreen();
        updateClock();
    }

    private void buildScreen() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(28), dp(20), dp(28), dp(20));
        root.setBackgroundColor(BG);

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout brand = new LinearLayout(this);
        brand.setOrientation(LinearLayout.VERTICAL);
        TextView logo = text("KOUP", 25, WHITE, true);
        logo.setLetterSpacing(0.18f);
        TextView subtitle = text("DRIVER INTERFACE", 10, MUTED, true);
        subtitle.setLetterSpacing(0.20f);
        brand.addView(logo);
        brand.addView(subtitle);
        header.addView(brand, new LinearLayout.LayoutParams(0, -2, 1));

        LinearLayout timeBox = new LinearLayout(this);
        timeBox.setOrientation(LinearLayout.VERTICAL);
        timeBox.setGravity(Gravity.RIGHT);
        clockView = text("--:--", 28, WHITE, false);
        dateView = text("--", 12, MUTED, false);
        timeBox.addView(clockView);
        timeBox.addView(dateView);
        header.addView(timeBox);
        root.addView(header);

        View line = new View(this);
        line.setBackgroundColor(Color.rgb(42, 45, 51));
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(-1, dp(1));
        lineParams.topMargin = dp(16);
        lineParams.bottomMargin = dp(18);
        root.addView(line, lineParams);

        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setPadding(dp(22), dp(18), dp(22), dp(18));
        hero.setGravity(Gravity.CENTER_VERTICAL);
        hero.setBackground(round(PANEL, dp(18), RED, dp(1)));

        TextView eyebrow = text("CONNECTED EXPERIENCE", 10, RED, true);
        eyebrow.setLetterSpacing(0.16f);
        TextView title = text("Your drive.\nYour space.", 27, WHITE, true);
        TextView hint = text("Open D-Play to connect your iPhone.", 13, MUTED, false);
        LinearLayout.LayoutParams titleP = new LinearLayout.LayoutParams(-1, -2);
        titleP.topMargin = dp(7);
        hero.addView(eyebrow);
        hero.addView(title, titleP);
        LinearLayout.LayoutParams hintP = new LinearLayout.LayoutParams(-1, -2);
        hintP.topMargin = dp(6);
        hero.addView(hint);

        Button dplay = button("OPEN D-PLAY  ↗", true);
        LinearLayout.LayoutParams dplayP = new LinearLayout.LayoutParams(-1, dp(48));
        dplayP.topMargin = dp(15);
        hero.addView(dplay, dplayP);
        dplay.setOnClickListener(v -> launchMatchingApp(new String[]{"d-play", "dplay", "di-play", "diplay"}, "D-Play"));

        LinearLayout.LayoutParams heroP = new LinearLayout.LayoutParams(-1, 0, 1.15f);
        root.addView(hero, heroP);

        TextView quickLabel = text("QUICK ACCESS", 10, MUTED, true);
        quickLabel.setLetterSpacing(0.18f);
        LinearLayout.LayoutParams ql = new LinearLayout.LayoutParams(-1, -2);
        ql.topMargin = dp(18);
        ql.bottomMargin = dp(10);
        root.addView(quickLabel, ql);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.addView(tile("MAP", "Navigation", "navigation"), weightParams());
        row.addView(spacer());
        row.addView(tile("♪", "Music", "music"), weightParams());
        row.addView(spacer());
        row.addView(tile("☎", "Phone", "phone"), weightParams());
        row.addView(spacer());
        row.addView(tile("•••", "Apps", "apps"), weightParams());
        root.addView(row, new LinearLayout.LayoutParams(-1, dp(92)));

        TextView footer = text("KOUP OS  •  CONCEPT BUILD 0.1", 9, Color.rgb(92, 97, 105), true);
        footer.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(-1, -2);
        fp.topMargin = dp(13);
        root.addView(footer, fp);

        setContentView(root);
    }

    private View tile(String symbol, String label, String action) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);
        box.setBackground(round(PANEL, dp(14), Color.rgb(36, 39, 45), dp(1)));
        TextView icon = text(symbol, 20, action.equals("apps") ? RED : WHITE, true);
        TextView name = text(label, 11, MUTED, true);
        LinearLayout.LayoutParams np = new LinearLayout.LayoutParams(-2, -2);
        np.topMargin = dp(7);
        box.addView(icon);
        box.addView(name, np);
        box.setOnClickListener(v -> {
            if ("apps".equals(action)) showApps();
            else if ("navigation".equals(action)) launchMatchingApp(
                    new String[]{"google maps", "maps", "waze", "here wego"}, "Navigation");
            else if ("music".equals(action)) launchMatchingApp(
                    new String[]{"spotify", "music", "poweramp", "youtube music"}, "Music");
            else if ("phone".equals(action)) launchMatchingApp(
                    new String[]{"phone", "dialer", "bluetooth"}, "Phone");
        });
        return box;
    }

    private LinearLayout.LayoutParams weightParams() {
        return new LinearLayout.LayoutParams(0, -1, 1);
    }

    private View spacer() {
        return new View(this);
    }

    private Button button(String label, boolean primary) {
        Button b = new Button(this);
        b.setText(label);
        b.setAllCaps(false);
        b.setTextSize(13);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setTextColor(primary ? WHITE : MUTED);
        b.setLetterSpacing(0.06f);
        b.setBackground(round(primary ? RED : PANEL, dp(12), primary ? RED : Color.rgb(40, 43, 49), dp(1)));
        return b;
    }

    private TextView text(String value, float size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        t.setTypeface(Typeface.create("sans-serif", bold ? Typeface.BOLD : Typeface.NORMAL));
        return t;
    }

    private GradientDrawable round(int color, int radius, int strokeColor, int strokeWidth) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        d.setStroke(strokeWidth, strokeColor);
        return d;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private void updateClock() {
        if (clockView == null || dateView == null) return;
        Date now = new Date();
        clockView.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(now));
        dateView.setText(new SimpleDateFormat("EEE, d MMM", Locale.getDefault()).format(now));
        clockView.postDelayed(this::updateClock, 30_000);
    }

    private List<ResolveInfo> getLaunchableApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return getPackageManager().queryIntentActivities(intent, 0);
    }

    private void launchMatchingApp(String[] terms, String friendlyName) {
        List<ResolveInfo> apps = getLaunchableApps();
        for (String term : terms) {
            for (ResolveInfo info : apps) {
                String label = info.loadLabel(getPackageManager()).toString().toLowerCase(Locale.ROOT);
                if (label.contains(term)) {
                    Intent launch = getPackageManager().getLaunchIntentForPackage(info.activityInfo.packageName);
                    if (launch != null) {
                        try {
                            startActivity(launch);
                            return;
                        } catch (Exception ignored) { }
                    }
                }
            }
        }
        Toast.makeText(this, friendlyName + " app not found. Open Apps to choose it.", Toast.LENGTH_LONG).show();
    }

    private void showApps() {
        List<ResolveInfo> apps = getLaunchableApps();
        final ArrayList<ResolveInfo> safeApps = new ArrayList<>();
        final ArrayList<String> labels = new ArrayList<>();
        for (ResolveInfo info : apps) {
            String label = info.loadLabel(getPackageManager()).toString();
            if (!label.equalsIgnoreCase("Koup OS")) {
                safeApps.add(info);
                labels.add(label);
            }
        }
        new AlertDialog.Builder(this)
                .setTitle("Open an app")
                .setItems(labels.toArray(new String[0]), (dialog, which) -> {
                    ResolveInfo info = safeApps.get(which);
                    Intent launch = getPackageManager().getLaunchIntentForPackage(info.activityInfo.packageName);
                    if (launch != null) {
                        try { startActivity(launch); }
                        catch (Exception e) { Toast.makeText(this, "Could not open app.", Toast.LENGTH_SHORT).show(); }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
