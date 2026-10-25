```java
package com.koup.os;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    private final int BG = Color.rgb(8, 9, 11);
    private final int PANEL = Color.rgb(19, 21, 25);
    private final int WHITE = Color.rgb(245, 246, 248);
    private final int MUTED = Color.rgb(151, 157, 166);
    private final int RED = Color.rgb(227, 74, 74);
    private final int BORDER = Color.rgb(42, 45, 51);

    private LinearLayout root;
    private TextView clockView;
    private TextView dateView;
    private String currentPage = "HOME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        showHome();
    }

    @Override
    public void onBackPressed() {
        if (!"HOME".equals(currentPage)) {
            showHome();
        } else {
            super.onBackPressed();
        }
    }

    private void showHome() {
        currentPage = "HOME";
        root = newRoot();

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout brand = new LinearLayout(this);
        brand.setOrientation(LinearLayout.VERTICAL);

        TextView logo = text("KOUP", 29, WHITE, true);
        logo.setLetterSpacing(0.20f);

        TextView sub = text("OS  /  DRIVER INTERFACE", 10, MUTED, true);
        sub.setLetterSpacing(0.13f);

        brand.addView(logo);
        brand.addView(sub);

        header.addView(brand, new LinearLayout.LayoutParams(0, -2, 1));

        LinearLayout time = new LinearLayout(this);
        time.setOrientation(LinearLayout.VERTICAL);
        time.setGravity(Gravity.RIGHT);

        clockView = text("--:--", 27, WHITE, false);
        dateView = text("--", 11, MUTED, false);

        time.addView(clockView);
        time.addView(dateView);
        header.addView(time);

        root.addView(header);

        addDivider(root, 15, 17);

        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setGravity(Gravity.CENTER_VERTICAL);
        hero.setPadding(dp(24), dp(20), dp(24), dp(20));
        hero.setBackground(round(PANEL, dp(20), RED, dp(1)));

        TextView eyebrow = text("YOUR DRIVE  /  YOUR SPACE", 10, RED, true);
        eyebrow.setLetterSpacing(0.15f);

        TextView title = text("Made for\nyour drive.", 31, WHITE, true);

        TextView description = text(
                "Your car. Your sound. Your experience.",
                12, MUTED, false
        );

        hero.addView(eyebrow);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(-1, -2);
        titleParams.topMargin = dp(8);
        hero.addView(title, titleParams);

        LinearLayout.LayoutParams descParams =
                new LinearLayout.LayoutParams(-1, -2);
        descParams.topMargin = dp(7);
        hero.addView(description, descParams);

        Button dplay = button("OPEN D-PLAY   ↗", true);

        LinearLayout.LayoutParams dplayParams =
                new LinearLayout.LayoutParams(-1, dp(49));
        dplayParams.topMargin = dp(18);
        hero.addView(dplay, dplayParams);

        dplay.setOnClickListener(v ->
                launchMatchingApp(
                        new String[]{"d-play", "dplay", "di-play", "diplay"},
                        "D-Play"
                )
        );

        LinearLayout.LayoutParams heroParams =
                new LinearLayout.LayoutParams(-1, 0, 1.0f);
        root.addView(hero, heroParams);

        TextView quick = text("QUICK ACCESS", 10, MUTED, true);
        quick.setLetterSpacing(0.18f);

        LinearLayout.LayoutParams quickParams =
                new LinearLayout.LayoutParams(-1, -2);
        quickParams.topMargin = dp(16);
        quickParams.bottomMargin = dp(9);
        root.addView(quick, quickParams);

        LinearLayout row1 = new LinearLayout(this);
        row1.setOrientation(LinearLayout.HORIZONTAL);

        row1.addView(tile("MAP", "Navigation",
                () -> showCategory("Navigation")),
                weightParams());

        row1.addView(spacer(), spacerParams());

        row1.addView(tile("♪", "Music",
                () -> showCategory("Music")),
                weightParams());

        row1.addView(spacer(), spacerParams());

        row1.addView(tile("☎", "Phone",
                () -> showCategory("Phone")),
                weightParams());

        row1.addView(spacer(), spacerParams());

        row1.addView(tile("•••", "Apps",
                this::showApps),
                weightParams());

        root.addView(row1, new LinearLayout.LayoutParams(-1, dp(88)));

        LinearLayout bottom = new LinearLayout(this);
        bottom.setOrientation(LinearLayout.HORIZONTAL);
        bottom.setGravity(Gravity.CENTER_VERTICAL);

        TextView footer = text("KOUP OS  /  0.2 CONCEPT", 9,
                Color.rgb(100, 105, 113), true);

        bottom.addView(footer, new LinearLayout.LayoutParams(0, -2, 1));

        TextView settings = text("SETTINGS  ⚙", 10, MUTED, true);
        settings.setPadding(dp(10), dp(10), dp(10), dp(10));
        settings.setOnClickListener(v -> showSettings());
        bottom.addView(settings);

        LinearLayout.LayoutParams bottomParams =
                new LinearLayout.LayoutParams(-1, -2);
        bottomParams.topMargin = dp(9);
        root.addView(bottom, bottomParams);

        setContentView(root);
        updateClock();
    }

    private void showCategory(String category) {
        currentPage = category.toUpperCase(Locale.ROOT);
        root = newRoot();
        addPageHeader(root, category.toUpperCase(Locale.ROOT));

        TextView title = text(category, 30, WHITE, true);
        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(-1, -2);
        titleParams.topMargin = dp(16);
        root.addView(title, titleParams);

        TextView description = text(
                "Choose an installed app to continue.",
                13, MUTED, false
        );
        LinearLayout.LayoutParams descParams =
                new LinearLayout.LayoutParams(-1, -2);
        descParams.topMargin = dp(5);
        root.addView(description, descParams);

        String[] terms;
        if ("Navigation".equals(category)) {
            terms = new String[]{"google maps", "maps", "waze", "here wego"};
        } else if ("Music".equals(category)) {
            terms = new String[]{"spotify", "music", "poweramp", "youtube music"};
        } else {
            terms = new String[]{"phone", "dialer", "bluetooth"};
        }

        Button recommended = button("OPEN " + category.toUpperCase(Locale.ROOT), true);
        LinearLayout.LayoutParams recParams =
                new LinearLayout.LayoutParams(-1, dp(52));
        recParams.topMargin = dp(22);
        root.addView(recommended, recParams);

        recommended.setOnClickListener(v ->
                launchMatchingApp(terms, category)
        );

        Button browse = button("CHOOSE INSTALLED APP", false);
        LinearLayout.LayoutParams browseParams =
                new LinearLayout.LayoutParams(-1, dp(50));
        browseParams.topMargin = dp(10);
        root.addView(browse, browseParams);
        browse.setOnClickListener(v -> showApps());

        addBottomBack(root);
        setContentView(root);
    }

    private void showApps() {
        currentPage = "APPS";
        root = newRoot();
        addPageHeader(root, "APPLICATIONS");

        TextView title = text("Your apps.", 29, WHITE, true);
        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(-1, -2);
        titleParams.topMargin = dp(14);
        root.addView(title, titleParams);

        TextView hint = text(
                "Select an installed application.",
                12, MUTED, false
        );
        LinearLayout.LayoutParams hintParams =
                new LinearLayout.LayoutParams(-1, -2);
        hintParams.topMargin = dp(5);
        hintParams.bottomMargin = dp(12);
        root.addView(hint, hintParams);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(false);

        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);

        List<ResolveInfo> apps = getLaunchableApps();

        Collections.sort(apps, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                String first = a.loadLabel(getPackageManager()).toString();
                String second = b.loadLabel(getPackageManager()).toString();
                return first.compareToIgnoreCase(second);
            }
        });

        for (ResolveInfo info : apps) {
            String label = info.loadLabel(getPackageManager()).toString();

            if ("Koup OS".equalsIgnoreCase(label)
                    || "KOUP OS".equalsIgnoreCase(label)) {
                continue;
            }

            LinearLayout item = new LinearLayout(this);
            item.setOrientation(LinearLayout.HORIZONTAL);
            item.setGravity(Gravity.CENTER_VERTICAL);
            item.setPadding(dp(15), dp(13), dp(15), dp(13));
            item.setBackground(round(PANEL, dp(12), BORDER, dp(1)));

            TextView appName = text(label, 15, WHITE, true);
            item.addView(appName, new LinearLayout.LayoutParams(0, -2, 1));

            TextView arrow = text("OPEN  ↗", 10, RED, true);
            item.addView(arrow);

            LinearLayout.LayoutParams itemParams =
                    new LinearLayout.LayoutParams(-1, -2);
            itemParams.bottomMargin = dp(7);
            list.addView(item, itemParams);

            final String packageName = info.activityInfo.packageName;
            item.setOnClickListener(v -> launchPackage(packageName));
        }

        scroll.addView(list);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));

        addBottomBack(root);
        setContentView(root);
    }

    private void showSettings() {
        currentPage = "SETTINGS";
        root = newRoot();
        addPageHeader(root, "PREFERENCES");

        TextView title = text("Settings.", 30, WHITE, true);
        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(-1, -2);
        titleParams.topMargin = dp(16);
        root.addView(title, titleParams);

        addSettingCard(root, "DISPLAY",
                "KOUP OS uses a dark interface designed for the car.");

        addSettingCard(root, "APPLICATIONS",
                "Manage installed apps using the Apps screen.");

        addSettingCard(root, "D-PLAY",
                "D-Play remains installed separately. This screen launches it.");

        addSettingCard(root, "ORIGINAL LAUNCHER",
                "Return to the head unit's original home screen.");

        Button original = button("OPEN ORIGINAL LAUNCHER", true);
        LinearLayout.LayoutParams originalParams =
                new LinearLayout.LayoutParams(-1, dp(50));
        originalParams.topMargin = dp(12);
        root.addView(original, originalParams);

        original.setOnClickListener(v -> openOriginalLauncher());

        addBottomBack(root);
        setContentView(root);
    }

    private void showAbout() {
        currentPage = "ABOUT";
        root = newRoot();
        addPageHeader(root, "KOUP OS");

        TextView logo = text("KOUP", 42, WHITE, true);
        logo.setLetterSpacing(0.22f);

        LinearLayout.LayoutParams logoParams =
                new LinearLayout.LayoutParams(-1, -2);
        logoParams.topMargin = dp(24);
        root.addView(logo, logoParams);

        TextView os = text("OPERATING EXPERIENCE", 11, RED, true);
        os.setLetterSpacing(0.16f);
        root.addView(os);

        TextView description = text(
                "A custom driver interface for your Kia Forte Koup.\n\n"
                        + "Designed around a clean dark theme, quick access "
                        + "to installed apps, and D-Play integration.\n\n"
                        + "Build: 0.2 concept\n"
                        + "Package: com.koup.os",
                14, MUTED, false
        );

        LinearLayout.LayoutParams descParams =
                new LinearLayout.LayoutParams(-1, -2);
        descParams.topMargin = dp(22);
        root.addView(description, descParams);

        addBottomBack(root);
        setContentView(root);
    }

    private LinearLayout newRoot() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(25), dp(17), dp(25), dp(14));
        layout.setBackgroundColor(BG);
        return layout;
    }

    private void addPageHeader(LinearLayout layout, String section) {
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);

        TextView brand = text("KOUP", 23, WHITE, true);
        brand.setLetterSpacing(0.17f);
        header.addView(brand, new LinearLayout.LayoutParams(0, -2, 1));

        TextView label = text(section, 10, RED, true);
        label.setLetterSpacing(0.09f);
        header.addView(label);

        layout.addView(header);
        addDivider(layout, 13, 3);
    }

    private void addSettingCard(LinearLayout layout,
                                String heading, String description) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(15), dp(13), dp(15), dp(13));
        card.setBackground(round(PANEL, dp(12), BORDER, dp(1)));

        TextView title = text(heading, 11, RED, true);
        title.setLetterSpacing(0.10f);
        card.addView(title);

        TextView body = text(description, 12, MUTED, false);
        LinearLayout.LayoutParams bodyParams =
                new LinearLayout.LayoutParams(-1, -2);
        bodyParams.topMargin = dp(5);
        card.addView(body, bodyParams);

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(-1, -2);
        cardParams.topMargin = dp(9);
        layout.addView(card, cardParams);
    }

    private void addBottomBack(LinearLayout layout) {
        Button back = button("←  HOME", false);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, dp(46));
        params.topMargin = dp(10);
        layout.addView(back, params);
        back.setOnClickListener(v -> showHome());
    }

    private void addDivider(LinearLayout layout,
                            int topMargin, int bottomMargin) {
        View line = new View(this);
        line.setBackgroundColor(BORDER);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, dp(1));
        params.topMargin = dp(topMargin);
        params.bottomMargin = dp(bottomMargin);
        layout.addView(line, params);
    }

    private View tile(String symbol, String label, Runnable action) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);
        box.setBackground(round(PANEL, dp(14), BORDER, dp(1)));

        TextView icon = text(symbol, 20, "Apps".equals(label) ? RED : WHITE, true);
        TextView name = text(label, 11, MUTED, true);

        box.addView(icon);

        LinearLayout.LayoutParams nameParams =
                new LinearLayout.LayoutParams(-2, -2);
        nameParams.topMargin = dp(6);
        box.addView(name, nameParams);

        box.setOnClickListener(v -> action.run());
        return box;
    }

    private LinearLayout.LayoutParams weightParams() {
        return new LinearLayout.LayoutParams(0, -1, 1);
    }

    private LinearLayout.LayoutParams spacerParams() {
        return new LinearLayout.LayoutParams(dp(8), 1);
    }

    private View spacer() {
        return new View(this);
    }

    private Button button(String label, boolean primary) {
        Button b = new Button(this);
        b.setText(label);
        b.setAllCaps(false);
        b.setTextSize(12);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setTextColor(primary ? WHITE : MUTED);
        b.setLetterSpacing(0.04f);
        b.setBackground(round(
                primary ? RED : PANEL,
                dp(12),
                primary ? RED : BORDER,
                dp(1)
        ));
        return b;
    }

    private TextView text(String value, float size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        t.setTypeface(Typeface.create(
                "sans-serif",
                bold ? Typeface.BOLD : Typeface.NORMAL
        ));
        return t;
    }

    private GradientDrawable round(int color, int radius,
                                   int strokeColor, int strokeWidth) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radius);
        d.setStroke(strokeWidth, strokeColor);
        return d;
    }

    private int dp(int value) {
        return Math.round(
                value * getResources().getDisplayMetrics().density
        );
    }

    private void updateClock() {
        if (clockView == null || dateView == null
                || !"HOME".equals(currentPage)) {
            return;
        }

        Date now = new Date();

        clockView.setText(
                new SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
        );

        dateView.setText(
                new SimpleDateFormat("EEE, d MMM", Locale.getDefault()).format(now)
        );

        clockView.postDelayed(this::updateClock, 30000);
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
                String label = info.loadLabel(getPackageManager())
                        .toString()
                        .toLowerCase(Locale.ROOT);

                if (label.contains(term)) {
                    Intent launch = getPackageManager()
                            .getLaunchIntentForPackage(
                                    info.activityInfo.packageName
                            );

                    if (launch != null) {
                        try {
                            startActivity(launch);
                            return;
                        } catch (Exception ignored) {
                            // Try another matching app.
                        }
                    }
                }
            }
        }

        Toast.makeText(
                this,
                friendlyName + " app not found. Open Apps to choose it.",
                Toast.LENGTH_LONG
        ).show();
    }

    private void launchPackage(String packageName) {
        Intent launch = getPackageManager().getLaunchIntentForPackage(packageName);

        if (launch == null) {
            Toast.makeText(this, "This app cannot be opened.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        try {
            startActivity(launch);
        } catch (Exception e) {
            Toast.makeText(this, "Could not open app.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openOriginalLauncher() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);

        List<ResolveInfo> homes = getPackageManager()
                .queryIntentActivities(home, 0);

        for (ResolveInfo info : homes) {
            if (!info.activityInfo.packageName.equals(getPackageName())) {
                Intent launch = getPackageManager()
                        .getLaunchIntentForPackage(
                                info.activityInfo.packageName
                        );

                if (launch != null) {
                    try {
                        startActivity(launch);
                        return;
                    } catch (Exception ignored) {
                        // Continue to the next available home app.
                    }
                }
            }
        }

        Toast.makeText(
                this,
                "Original launcher not found in installed apps.",
                Toast.LENGTH_LONG
        ).show();
    }
}
```
