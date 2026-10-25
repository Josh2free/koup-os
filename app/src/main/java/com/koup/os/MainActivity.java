package com.koup.os;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
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

    private static final int BG = Color.rgb(10, 11, 13);
    private static final int PANEL = Color.rgb(21, 23, 27);
    private static final int PANEL2 = Color.rgb(29, 31, 36);
    private static final int RED = Color.rgb(229, 43, 48);
    private static final int WHITE = Color.rgb(246, 247, 249);
    private static final int MUTED = Color.rgb(151, 155, 163);
    private static final int LINE = Color.rgb(47, 49, 55);

    private LinearLayout root;
    private String currentScreen = "HOME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        showHome();
    }

    private void setup(String screen) {
        currentScreen = screen;

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);
        root.setPadding(dp(24), dp(14), dp(24), dp(12));

        setContentView(root);
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private GradientDrawable shape(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        return drawable;
    }

    private GradientDrawable outline(int color, int radius) {
        GradientDrawable drawable = shape(color, radius);
        drawable.setStroke(dp(1), LINE);
        return drawable;
    }

    private TextView text(String value, float size, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setFontFeatureSettings("kern");
        view.setTypeface(
                Typeface.create(bold ? "sans-serif" : "sans-serif-light",
                        bold ? Typeface.BOLD : Typeface.NORMAL)
        );
        return view;
    }

    private TextView label(String value) {
        TextView view = text(value.toUpperCase(Locale.US), 10, MUTED, true);
        view.setLetterSpacing(0.15f);
        return view;
    }

    private void gap(LinearLayout parent, int height) {
        View spacer = new View(this);
        parent.addView(spacer, new LinearLayout.LayoutParams(
                1, dp(height)
        ));
    }

    private View divider() {
        View view = new View(this);
        view.setBackgroundColor(LINE);
        return view;
    }

    private LinearLayout row() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        return row;
    }

    private LinearLayout column() {
        LinearLayout column = new LinearLayout(this);
        column.setOrientation(LinearLayout.VERTICAL);
        return column;
    }

    private void addWeighted(LinearLayout parent, View child, float weight) {
        parent.addView(child, new LinearLayout.LayoutParams(
                0, -1, weight
        ));
    }

    private Button button(String title, boolean primary, Runnable action) {
        Button button = new Button(this);
        button.setText(title);
        button.setTextSize(12);
        button.setAllCaps(false);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setTextColor(WHITE);
        button.setGravity(Gravity.CENTER);
        button.setPadding(dp(12), 0, dp(12), 0);
        button.setBackground(primary ? shape(RED, 12) : outline(PANEL2, 12));
        button.setOnClickListener(v -> action.run());
        return button;
    }

    private LinearLayout card(String title, String subtitle, String symbol,
                              Runnable action) {
        LinearLayout card = column();
        card.setPadding(dp(16), dp(13), dp(16), dp(13));
        card.setBackground(outline(PANEL, 13));
        card.setClickable(true);
        card.setFocusable(true);

        TextView icon = text(symbol, 22, RED, true);
        card.addView(icon, new LinearLayout.LayoutParams(
                -1, dp(29)
        ));

        gap(card, 7);

        card.addView(text(title, 15, WHITE, true));

        if (subtitle != null && !subtitle.isEmpty()) {
            gap(card, 3);
            card.addView(text(subtitle, 11, MUTED, false));
        }

        card.setOnClickListener(v -> action.run());
        return card;
    }

    private void addHeader(String title, String subtitle) {
        LinearLayout header = row();

        LinearLayout titles = column();
        titles.addView(text(title, 22, WHITE, true));
        titles.addView(text(subtitle, 11, MUTED, false));

        header.addView(titles, new LinearLayout.LayoutParams(
                0, -2, 1
        ));

        header.addView(button("‹  HOME", false, this::showHome),
                new LinearLayout.LayoutParams(dp(105), dp(40)));

        root.addView(header);
        gap(root, 13);
        root.addView(divider(), new LinearLayout.LayoutParams(-1, dp(1)));
        gap(root, 15);
    }

    private void addFooter() {
        LinearLayout footer = row();
        footer.setGravity(Gravity.CENTER_VERTICAL);

        TextView brand = text("KOUP OS", 10, MUTED, true);
        footer.addView(brand, new LinearLayout.LayoutParams(
                0, dp(24), 1
        ));

        footer.addView(text("YOUR DRIVE  •  YOUR WAY", 9, MUTED, true));

        root.addView(footer, new LinearLayout.LayoutParams(-1, dp(25)));
    }

    // HOME — premium dark cockpit layout with custom coupe artwork.
    private void showHome() {
        setup("HOME");

        LinearLayout top = row();

        LinearLayout brand = column();
        TextView logo = text("KOUP", 29, WHITE, true);
        logo.setLetterSpacing(0.12f);
        brand.addView(logo);

        TextView os = text("O S", 10, RED, true);
        os.setLetterSpacing(0.45f);
        brand.addView(os);

        top.addView(brand, new LinearLayout.LayoutParams(
                0, -2, 1
        ));

        LinearLayout clock = column();
        clock.setGravity(Gravity.RIGHT);

        String time = new SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(new Date());
        String date = new SimpleDateFormat("EEE, dd MMM yyyy",
                Locale.getDefault()).format(new Date());

        clock.addView(text(time, 27, WHITE, true));
        clock.addView(text(date, 10, MUTED, false));
        top.addView(clock);

        root.addView(top);
        gap(root, 13);

        LinearLayout hero = row();
        hero.setPadding(dp(20), dp(13), dp(12), dp(8));
        hero.setBackground(shape(PANEL, 18));

        LinearLayout heroText = column();
        heroText.setGravity(Gravity.CENTER_VERTICAL);

        TextView eyebrow = text("YOUR DRIVE  •  YOUR WAY", 10, RED, true);
        eyebrow.setLetterSpacing(0.13f);
        heroText.addView(eyebrow);

        gap(heroText, 7);

        TextView headline = text("Made for\nyour drive.", 25, WHITE, true);
        headline.setLineSpacing(dp(2), 1f);
        heroText.addView(headline);

        gap(heroText, 7);

        heroText.addView(text("A cleaner way to stay connected.",
                11, MUTED, false));

        gap(heroText, 12);

        Button play = button("▶   OPEN D-PLAY", true, this::openDPlay);
        heroText.addView(play, new LinearLayout.LayoutParams(
                -1, dp(43)
        ));

        hero.addView(heroText, new LinearLayout.LayoutParams(
                0, -1, 0.88f
        ));

        CoupeView coupe = new CoupeView();
        hero.addView(coupe, new LinearLayout.LayoutParams(
                0, -1, 1.12f
        ));

        root.addView(hero, new LinearLayout.LayoutParams(
                -1, 0, 1.45f
        ));

        gap(root, 13);

        root.addView(label("QUICK ACCESS"));
        gap(root, 8);

        LinearLayout quick = row();

        addWeighted(quick, card("Navigation", "Find your way", "↗",
                () -> showCategory("NAVIGATION")), 1);
        addWeighted(quick, card("Music", "Your soundtrack", "♫",
                () -> showCategory("MUSIC")), 1);
        addWeighted(quick, card("Phone", "Calls & contacts", "☎",
                () -> showCategory("PHONE")), 1);
        addWeighted(quick, card("Apps", "All applications", "▦",
                this::showApps), 1);

        root.addView(quick, new LinearLayout.LayoutParams(
                -1, 0, 0.8f
        ));

        gap(root, 11);

        LinearLayout bottom = row();
        bottom.addView(text("READY FOR THE ROAD", 9, MUTED, true),
                new LinearLayout.LayoutParams(0, dp(30), 1));
        bottom.addView(button("⚙  SETTINGS", false, this::showSettings),
                new LinearLayout.LayoutParams(dp(135), dp(36)));

        root.addView(bottom);
    }

    // Coupe silhouette drawn locally. No downloaded image is required.
    private class CoupeView extends View {
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        CoupeView() {
            super(MainActivity.this);
        }

        private void stroke(int color, float width) {
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(width);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            float w = getWidth();
            float h = getHeight();

            canvas.save();
            canvas.scale(w / 340f, h / 205f);

            // Low red ambient glow under the car.
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.rgb(57, 18, 22));
            canvas.drawOval(25, 153, 318, 190, paint);

            // Body silhouette.
            Path body = new Path();
            body.moveTo(16, 143);
            body.lineTo(35, 128);
            body.lineTo(75, 117);
            body.lineTo(111, 83);
            body.quadTo(126, 70, 151, 68);
            body.lineTo(206, 72);
            body.quadTo(222, 75, 241, 96);
            body.lineTo(284, 111);
            body.quadTo(311, 119, 321, 137);
            body.lineTo(320, 153);
            body.lineTo(21, 153);
            body.close();

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.rgb(37, 39, 44));
            canvas.drawPath(body, paint);

            stroke(RED, 2.4f);
            canvas.drawPath(body, paint);

            // Windows.
            Path glass = new Path();
            glass.moveTo(88, 114);
            glass.lineTo(119, 84);
            glass.quadTo(129, 76, 151, 76);
            glass.lineTo(169, 77);
            glass.lineTo(169, 113);
            glass.close();

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.rgb(13, 17, 22));
            canvas.drawPath(glass, paint);

            Path sideGlass = new Path();
            sideGlass.moveTo(176, 78);
            sideGlass.lineTo(204, 80);
            sideGlass.quadTo(218, 83, 232, 101);
            sideGlass.lineTo(244, 112);
            sideGlass.lineTo(176, 112);
            sideGlass.close();
            canvas.drawPath(sideGlass, paint);

            stroke(Color.rgb(94, 96, 103), 1.2f);
            canvas.drawLine(173, 76, 173, 148, paint);
            canvas.drawLine(90, 117, 245, 117, paint);

            // Headlight and rear lamp.
            stroke(WHITE, 2.2f);
            canvas.drawLine(296, 129, 314, 134, paint);

            stroke(RED, 3.2f);
            canvas.drawLine(23, 132, 41, 127, paint);

            // Wheels.
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.rgb(7, 8, 10));
            canvas.drawCircle(82, 149, 25, paint);
            canvas.drawCircle(258, 149, 25, paint);

            stroke(Color.rgb(119, 122, 130), 2.2f);
            canvas.drawCircle(82, 149, 17, paint);
            canvas.drawCircle(258, 149, 17, paint);

            stroke(RED, 1.5f);
            canvas.drawCircle(82, 149, 7, paint);
            canvas.drawCircle(258, 149, 7, paint);

            stroke(Color.rgb(105, 108, 115), 1.1f);
            canvas.drawLine(82, 133, 82, 165, paint);
            canvas.drawLine(66, 149, 98, 149, paint);
            canvas.drawLine(258, 133, 258, 165, paint);
            canvas.drawLine(242, 149, 274, 149, paint);

            // Ground line.
            stroke(Color.rgb(67, 40, 43), 1f);
            canvas.drawLine(12, 180, 328, 180, paint);

            canvas.restore();
        }
    }

    private void showCategory(String category) {
        setup(category);
        addHeader(category, categorySubtitle(category));

        LinearLayout content = column();
        content.setGravity(Gravity.CENTER);
        content.setPadding(dp(15), dp(12), dp(15), dp(12));

        TextView symbol;
        String title;
        String subtitle;
        String primary;

        if ("NAVIGATION".equals(category)) {
            symbol = text("↗", 54, RED, true);
            title = "Every journey starts here.";
            subtitle = "Open your installed navigation app to plan your route.";
            primary = "OPEN NAVIGATION";
        } else if ("MUSIC".equals(category)) {
            symbol = text("♫", 54, RED, true);
            title = "Set the soundtrack.";
            subtitle = "Open your music player and get back to your drive.";
            primary = "OPEN MUSIC";
        } else {
            symbol = text("☎", 54, RED, true);
            title = "Stay connected.";
            subtitle = "Open your phone or contacts app.";
            primary = "OPEN PHONE";
        }

        content.addView(symbol);
        gap(content, 10);

        TextView heading = text(title, 23, WHITE, true);
        heading.setGravity(Gravity.CENTER);
        content.addView(heading);

        gap(content, 8);

        TextView description = text(subtitle, 12, MUTED, false);
        description.setGravity(Gravity.CENTER);
        content.addView(description);

        gap(content, 22);

        content.addView(button(primary, true, () -> launchCategory(category)),
                new LinearLayout.LayoutParams(dp(230), dp(46)));

        gap(content, 10);

        content.addView(button("BROWSE INSTALLED APPS", false, this::showApps),
                new LinearLayout.LayoutParams(dp(230), dp(42)));

        root.addView(content, new LinearLayout.LayoutParams(
                -1, 0, 1
        ));

        addFooter();
    }

    private String categorySubtitle(String category) {
        if ("NAVIGATION".equals(category)) return "YOUR NEXT DESTINATION";
        if ("MUSIC".equals(category)) return "YOUR SOUNDTRACK";
        return "CALLS & CONTACTS";
    }

    private void launchCategory(String category) {
        String[] keys;

        if ("NAVIGATION".equals(category)) {
            keys = new String[]{"maps", "navigation", "waze"};
        } else if ("MUSIC".equals(category)) {
            keys = new String[]{"music", "spotify", "audio", "radio"};
        } else {
            keys = new String[]{"phone", "dialer", "contacts"};
        }

        if (!launchMatching(keys)) {
            new AlertDialog.Builder(this)
                    .setTitle("App not found")
                    .setMessage("No matching installed app was found. Open Apps to choose an available application.")
                    .setPositiveButton("OPEN APPS", (d, w) -> showApps())
                    .setNegativeButton("CLOSE", null)
                    .show();
        }
    }

    private void openDPlay() {
        if (!launchMatching(new String[]{"d-play", "dplay", "d play"})) {
            new AlertDialog.Builder(this)
                    .setTitle("D-Play not found")
                    .setMessage("D-Play could not be found among launchable apps. Open Apps to check the installed applications.")
                    .setPositiveButton("OPEN APPS", (d, w) -> showApps())
                    .setNegativeButton("CLOSE", null)
                    .show();
        }
    }

    private boolean launchMatching(String[] keys) {
        List<ResolveInfo> apps = getLaunchableApps();

        for (String key : keys) {
            for (ResolveInfo info : apps) {
                String name = info.loadLabel(getPackageManager())
                        .toString().toLowerCase(Locale.US);

                if (name.contains(key.toLowerCase(Locale.US))) {
                    Intent intent = getPackageManager()
                            .getLaunchIntentForPackage(
                                    info.activityInfo.packageName
                            );

                    if (intent != null) {
                        try {
                            startActivity(intent);
                            return true;
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        }

        return false;
    }

    private List<ResolveInfo> getLaunchableApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps = getPackageManager()
                .queryIntentActivities(intent, 0);

        Collections.sort(apps, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                return a.loadLabel(getPackageManager()).toString()
                        .compareToIgnoreCase(
                                b.loadLabel(getPackageManager()).toString()
                        );
            }
        });

        return apps;
    }

    private void showApps() {
        setup("APPS");
        addHeader("Apps", "ALL INSTALLED APPLICATIONS");

        ScrollView scroll = new ScrollView(this);
        LinearLayout list = column();
        list.setPadding(0, 0, 0, dp(8));

        List<ResolveInfo> apps = getLaunchableApps();
        boolean found = false;

        for (ResolveInfo info : apps) {
            if (info.activityInfo.packageName.equals(getPackageName())) {
                continue;
            }

            found = true;

            String appName = info.loadLabel(getPackageManager()).toString();
            String packageName = info.activityInfo.packageName;

            LinearLayout item = row();
            item.setPadding(dp(13), dp(8), dp(13), dp(8));
            item.setBackground(outline(PANEL, 11));

            TextView initial = text(
                    appName.length() > 0
                            ? appName.substring(0, 1).toUpperCase(Locale.US)
                            : "•",
                    21, RED, true
            );
            initial.setGravity(Gravity.CENTER);
            initial.setBackground(shape(PANEL2, 10));

            item.addView(initial, new LinearLayout.LayoutParams(
                    dp(42), dp(42)
            ));

            LinearLayout details = column();
            details.setGravity(Gravity.CENTER_VERTICAL);
            details.setPadding(dp(12), 0, 0, 0);
            details.addView(text(appName, 14, WHITE, true));
            details.addView(text(packageName, 9, MUTED, false));

            item.addView(details, new LinearLayout.LayoutParams(
                    0, dp(46), 1
            ));

            item.addView(text("›", 25, MUTED, false));

            item.setOnClickListener(v -> {
                Intent launch = getPackageManager()
                        .getLaunchIntentForPackage(packageName);

                if (launch != null) {
                    try {
                        startActivity(launch);
                    } catch (Exception e) {
                        Toast.makeText(this, "Unable to open app",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            LinearLayout.LayoutParams itemParams =
                    new LinearLayout.LayoutParams(-1, dp(62));
            itemParams.bottomMargin = dp(7);
            list.addView(item, itemParams);
        }

        if (!found) {
            TextView empty = text("No launchable applications found.",
                    14, MUTED, false);
            list.addView(empty);
        }

        scroll.addView(list);
        root.addView(scroll, new LinearLayout.LayoutParams(
                -1, 0, 1
        ));

        addFooter();
    }

    private void showSettings() {
        setup("SETTINGS");
        addHeader("Settings", "PERSONALISE YOUR KOUP OS EXPERIENCE");

        ScrollView scroll = new ScrollView(this);
        LinearLayout content = column();

        addSetting(content, "Display & interface",
                "KOUP OS dark theme and visual style",
                "◐", () -> showInfo("Display & interface",
                        "KOUP OS uses the approved dark automotive theme. The interface is designed for a landscape head unit."));

        addSetting(content, "Installed applications",
                "Browse apps available on this head unit",
                "▦", this::showApps);

        addSetting(content, "D-Play",
                "Launch the installed D-Play application",
                "▶", this::openDPlay);

        addSetting(content, "Original launcher",
                "Return to the head unit's Android home screen",
                "⌂", this::openOriginalLauncher);

        addSetting(content, "About KOUP OS",
                "Version and project information",
                "i", this::showAbout);

        scroll.addView(content);

        root.addView(scroll, new LinearLayout.LayoutParams(
                -1, 0, 1
        ));

        addFooter();
    }

    private void addSetting(LinearLayout parent, String title,
                            String subtitle, String symbol, Runnable action) {
        LinearLayout item = row();
        item.setPadding(dp(14), dp(8), dp(14), dp(8));
        item.setBackground(outline(PANEL, 12));

        TextView icon = text(symbol, 24, RED, true);
        icon.setGravity(Gravity.CENTER);

        item.addView(icon, new LinearLayout.LayoutParams(dp(43), dp(44)));

        LinearLayout labels = column();
        labels.setGravity(Gravity.CENTER_VERTICAL);
        labels.setPadding(dp(12), 0, 0, 0);
        labels.addView(text(title, 14, WHITE, true));
        labels.addView(text(subtitle, 10, MUTED, false));

        item.addView(labels, new LinearLayout.LayoutParams(
                0, dp(46), 1
        ));
        item.addView(text("›", 24, MUTED, false));

        item.setOnClickListener(v -> action.run());

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, dp(62));
        params.bottomMargin = dp(8);
        parent.addView(item, params);
    }

    private void showInfo(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void showAbout() {
        setup("ABOUT");
        addHeader("About KOUP OS", "MADE FOR YOUR DRIVE");

        LinearLayout content = column();
        content.setGravity(Gravity.CENTER);
        content.setPadding(dp(20), dp(10), dp(20), dp(10));

        TextView logo = text("KOUP", 43, WHITE, true);
        logo.setGravity(Gravity.CENTER);
        logo.setLetterSpacing(0.16f);
        content.addView(logo);

        TextView os = text("O S", 12, RED, true);
        os.setGravity(Gravity.CENTER);
        os.setLetterSpacing(0.45f);
        content.addView(os);

        gap(content, 15);

        TextView slogan = text("YOUR DRIVE  •  YOUR WAY", 11, MUTED, true);
        slogan.setGravity(Gravity.CENTER);
        content.addView(slogan);

        gap(content, 20);

        content.addView(text("Version 0.2", 13, WHITE, true));
        gap(content, 7);
        content.addView(text("A custom interface for your Kia Forte Koup.",
                12, MUTED, false));

        gap(content, 18);

        content.addView(button("BACK TO HOME", true, this::showHome),
                new LinearLayout.LayoutParams(dp(200), dp(43)));

        root.addView(content, new LinearLayout.LayoutParams(
                -1, 0, 1
        ));

        addFooter();
    }

    private void openOriginalLauncher() {
        try {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(home);
        } catch (Exception e) {
            showInfo("Original launcher",
                    "Android could not open the original launcher automatically. Use the head unit's Home button or Android system navigation to return.");
        }
    }

    private void showDPlayScreen() {
        setup("D-PLAY");
        addHeader("D-Play", "YOUR CONNECTED DRIVE");

        LinearLayout content = column();
        content.setGravity(Gravity.CENTER);

        TextView mark = text("▶", 58, RED, true);
        mark.setGravity(Gravity.CENTER);
        content.addView(mark);

        gap(content, 12);

        TextView title = text("Ready when you are.", 24, WHITE, true);
        title.setGravity(Gravity.CENTER);
        content.addView(title);

        gap(content, 8);

        TextView desc = text("Launch the D-Play app installed on your head unit.",
                12, MUTED, false);
        desc.setGravity(Gravity.CENTER);
        content.addView(desc);

        gap(content, 22);

        content.addView(button("LAUNCH D-PLAY", true, this::openDPlay),
                new LinearLayout.LayoutParams(dp(230), dp(46)));

        root.addView(content, new LinearLayout.LayoutParams(-1, 0, 1));
        addFooter();
    }

    @Override
    public void onBackPressed() {
        if ("HOME".equals(currentScreen)) {
            super.onBackPressed();
        } else {
            showHome();
        }
    }
}
