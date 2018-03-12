package com.wind.me.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

public class VerticalLayoutTextView extends View {

    private static final int DOUBLE_SEP_LINE_HEIGHT = 90;
    private static final int DOUBLE_SPE_LINE_PADDING = 9;
    private int ENGLISH_CHAR_PADDING;
    private int mNormalCharPadding;
    private String mAlwaysTransChars;
    private String mText;
    private int mCustomMaxHeight;
    private float mFontBaselinePadding;
    private int mFontHeight;
    private int mHeight;
    private int mLastCanShowCharIndex;
    private Paint mLeftLinePaint;
    private int mLineSpace;
    private int mLineWidth;
    private int mMaxHeight;
    private int mMaxLines;
    private boolean mNeedCollipseEnd;
    private boolean mNeedLeftLine;
    private boolean mNeedSepLine;
    private int mNormalCharPaddingTop;
    private OnRealLineChangeListener mOnRealLineChangeListener;
    private Paint mPaint;
    private Paint mSepPaint;
    private int mTextSize;
    private int mTextColor;
    private String mTransAfterEngChars;
    private int mWidth;
    private static final int DEFAULT_TEXT_SIZE = 15;
    private static final int DEFAULT_COLOR = Color.BLACK;

    private static final int DEFAULT_CUSTOM_MAX_HEIGHT = Integer.MAX_VALUE;
    private static final int DEFAULT_MAX_LINES = 7;
    private static final int DEFAULT_NORMAL_CHAR_PADDINGTOP = 0;
    private static final int DEFAULT_NORMAL_CHAR_PADDING = 0;
    private static final int DEFAULT_LINE_SPACE = 0;

    public interface OnRealLineChangeListener {
        void realLineChange(int i);
    }

    public VerticalLayoutTextView(Context context) {
        this(context, null);
    }

    public VerticalLayoutTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.VerticalLayoutTextView);

        mTextColor = a.getColor(R.styleable.VerticalLayoutTextView_textColor, DEFAULT_COLOR);
        mTextSize = a.getDimensionPixelSize(R.styleable.VerticalLayoutTextView_textSize,
                DEFAULT_TEXT_SIZE);
        mNeedSepLine = a.getBoolean(R.styleable.VerticalLayoutTextView_needSepLine, false);
        mText = a.getString(R.styleable.VerticalLayoutTextView_text);
        mNeedCollipseEnd = a.getBoolean(R.styleable.VerticalLayoutTextView_needCollipseEnd, false);
        mCustomMaxHeight = a.getDimensionPixelSize(R.styleable.VerticalLayoutTextView_customMaxHeight, DEFAULT_CUSTOM_MAX_HEIGHT);
        mMaxLines = a.getInt(R.styleable.VerticalLayoutTextView_maxLines, DEFAULT_MAX_LINES);
        mNormalCharPaddingTop = a.getDimensionPixelSize(R.styleable.VerticalLayoutTextView_normalCharPaddingTop,
                DEFAULT_NORMAL_CHAR_PADDINGTOP);
        mNeedLeftLine = a.getBoolean(R.styleable.VerticalLayoutTextView_needLeftLine, false);
        mNormalCharPadding = a.getDimensionPixelSize(R.styleable.VerticalLayoutTextView_normalCharPadding, DEFAULT_NORMAL_CHAR_PADDING);
        mLineSpace = a.getDimensionPixelSize(R.styleable.VerticalLayoutTextView_lineSpace, DEFAULT_LINE_SPACE);

        a.recycle();

        setTextColor(mTextColor);
        setTextSize(mTextSize);
        setNeedLeftLine(mNeedLeftLine);
    }

    public VerticalLayoutTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mWidth = 0;
        this.mHeight = 0;
        this.mFontHeight = 0;
        this.mLineWidth = 0;
        this.mText = "";
        init();
    }

    public void setOnRealLineChangeListener(OnRealLineChangeListener onRealLineChangeListener) {
        this.mOnRealLineChangeListener = onRealLineChangeListener;
    }

    public void setNeedCollipseEnd(boolean need) {
        this.mNeedCollipseEnd = need;
    }

    public void setText(String contentText) {
        this.mText = contentText;
        requestLayout();
        invalidate();
    }

    public void setCustomMaxHeight(int maxHeight) {
        mCustomMaxHeight = dip2px(getContext(),maxHeight);
    }

    public void setMaxLines(int maxLines) {
        mMaxLines = maxLines;
    }

    public void setNormalCharPaddingTop(int normalCharPaddingTop) {
        mNormalCharPaddingTop = dip2px(getContext(),normalCharPaddingTop);
    }

    public void setNeedLeftLine(boolean needLeftLine) {
        mNeedLeftLine = needLeftLine;
        if (mNeedLeftLine) {
            mLeftLinePaint = new Paint(1);
            mLeftLinePaint.setStrokeWidth((float) dip2px(getContext(), 2.0f));
            mLeftLinePaint.setStyle(Style.STROKE);
            mLeftLinePaint.setColor(Color.BLACK);
        }
    }

    public void setTextSize(int textSize) {
        mTextSize =  textSize;
        mPaint.setTextSize((float) textSize);
    }

    public void setTextColor(int textColor) {
        mPaint.setColor(textColor);
        mSepPaint.setColor(textColor);
    }

    public void setTypeface(Typeface typeface) {
        mPaint.setTypeface(typeface);
    }

    public void setCharPadding(int padding) {
        mNormalCharPadding = padding;
    }

    public void setLineSpace(int lineSpace) {
        mLineSpace = dip2px(getContext(),lineSpace);
    }

    private void init() {
        this.mPaint = new Paint();
        this.mPaint.setTextAlign(Align.CENTER);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.BLUE);
        this.mPaint.setTextSize(24.0f);
        this.ENGLISH_CHAR_PADDING = 0;
        this.mSepPaint = new Paint(1);
        this.mSepPaint.setColor(Color.BLUE);
        this.mSepPaint.setStrokeWidth(3.0f);
        this.mSepPaint.setStyle(Style.STROKE);
        char[] china = new char[]{',', '.', '!', '\"', '\"', '[', ']', '(', ')', ':', '\'', '\\', '/', '·', '，', '。', '！', '“', '”', '［', '］', '（', '）', '：', '、', '／'};
        this.mAlwaysTransChars = String.valueOf(new char[]{'\"', '\"', '[', ']', '(', ')', '\'', '“', '”', '［', '］', '（', '）', '《', '》'});
        this.mTransAfterEngChars = String.valueOf(china);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mHeight = getLayoutParams().height;

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (mCustomMaxHeight == 0 || mCustomMaxHeight > heightSize) {
            mCustomMaxHeight = heightSize;
        }

        this.mWidth = measureWidth();
        setMeasuredDimension(this.mWidth, this.mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMultipleVerticalText(canvas);
        if (this.mNeedLeftLine) {
            canvas.drawLine(this.mLeftLinePaint.getStrokeWidth() / 2.0f, (float) dip2px(getContext(), 1.0f), this.mLeftLinePaint.getStrokeWidth() / 2.0f, (float) this.mMaxHeight, this.mLeftLinePaint);
        }
    }

    private float getNormalCharPaddingTop(float charHeight) {
        if (this.mNormalCharPaddingTop != 0) {
            return (float) this.mNormalCharPaddingTop;
        }
        return charHeight;
    }

    private boolean isLastCharEnglish(int i) {
        return i > 1 && isEnglishChar(this.mText.charAt(i - 1));
    }

    private void drawMultipleVerticalText(Canvas canvas) {
        int mTextPosY = 0;
        int mTextPosX = (this.mWidth - this.mLineWidth) + (this.mLineWidth / 2);
        int i = 0;
        while (i < this.mText.length()) {
            char ch = this.mText.charAt(i);
            if (this.mLastCanShowCharIndex == 0 || i != this.mLastCanShowCharIndex) {
                if (i == 0 && this.mNeedSepLine) {
                    mTextPosY += 99;
                    canvas.drawLine((float) mTextPosX, 0.0f, (float) mTextPosX, 90.0f, this.mPaint);
                }
                if (ch == '\n') {
                    mTextPosX = (mTextPosX - this.mLineWidth) - this.mLineSpace;
                    mTextPosY = 0;
                } else {
                    boolean isLastCharEnglish = isLastCharEnglish(i);
                    boolean isSpecialChar = isSpecialChar(isLastCharEnglish, ch);
                    float charHeight = getCharHeight(ch, isLastCharEnglish);
                    if (mTextPosY == 0 || i == 0) {
                        mTextPosY = (int) ((isSpecialChar ? 3.0f : getNormalCharPaddingTop(3.0f)) + ((float) mTextPosY));
                    }
                    mTextPosY = (int) (((float) mTextPosY) + charHeight);
                    if (i > 0 && isEnglishChar(ch) && ((float) mTextPosY) + charHeight > ((float) this.mHeight)) {
                        if (isEnglishChar(this.mText.charAt(i - 1))) {
                            canvas.drawLine((float) mTextPosX, 3.0f + (((float) mTextPosY) - charHeight), (float) mTextPosX, (float) (mTextPosY - 3), this.mPaint);
                        }
                        mTextPosX = (mTextPosX - this.mLineWidth) - this.mLineSpace;
                        i--;
                        mTextPosY = 0;
                    } else if (mTextPosY > this.mHeight) {
                        mTextPosX = (mTextPosX - this.mLineWidth) - this.mLineSpace;
                        i--;
                        mTextPosY = 0;
                    } else if (isSpecialChar) {
                        canvas.save();
                        canvas.translate((float) mTextPosX, ((float) mTextPosY));
                        canvas.rotate(90.0f);
                        this.mPaint.setTextSize(this.mTextSize + ((float) dip2px(getContext(), 1.0f)));
                        canvas.drawText(String.valueOf(ch), -(charHeight / 2.0f), (float) (this.mLineWidth / 3), this.mPaint);
                        this.mPaint.setTextSize(this.mTextSize);
                        canvas.restore();
                        mTextPosY += this.ENGLISH_CHAR_PADDING;
                    } else if (ch != ' ') {
                        canvas.drawText(String.valueOf(ch), (float) mTextPosX, ((float) mTextPosY) - this.mFontBaselinePadding, this.mPaint);
                        mTextPosY += this.mNormalCharPadding;
                    }
                }
                i++;
            } else if (this.mNeedCollipseEnd) {
                if (this.mNormalCharPaddingTop < 0) {
                    mTextPosY -= this.mNormalCharPaddingTop * 2;
                }
                this.mSepPaint.setStyle(Style.FILL);
                float dotSize = (this.mPaint.getTextSize() / 39.0f) * 6.0f;
                int posY = (int) ((isLastCharEnglish(i) ? dotSize : 0.0f) + ((float) mTextPosY));
                canvas.drawCircle((float) mTextPosX, (float) posY, dotSize / 2.0f, this.mSepPaint);
                canvas.drawCircle((float) mTextPosX, ((float) posY) + (2.0f * dotSize), dotSize / 2.0f, this.mSepPaint);
                canvas.drawCircle((float) mTextPosX, ((float) posY) + (4.0f * dotSize), dotSize / 2.0f, this.mSepPaint);
                this.mSepPaint.setStyle(Style.STROKE);
                return;
            } else {
                return;
            }
        }
    }

    private int measureWidth() {
        if (TextUtils.isEmpty(mText)) {
            return 0;
        }
        int dip2px;
        int h = 0;
        int lineSpaceCount = 0;
        measureLineWidth();
        measureFontHeight();
        int realLine = 1;
        int contentLength = this.mText.length();
        int i = 0;
        while (i < contentLength) {
            if (i == 0 && this.mNeedSepLine) {
                h = (h + DOUBLE_SEP_LINE_HEIGHT) + DOUBLE_SPE_LINE_PADDING;
            }
            char ch = this.mText.charAt(i);
            if (ch == '\n') {
                if (h > this.mMaxHeight) {
                    this.mMaxHeight = h;
                }
                if (i == contentLength || (this.mMaxLines != 0 && realLine == this.mMaxLines)) {
                    break;
                }
                realLine++;
                lineSpaceCount++;
                h = 0;
            } else {
                boolean isLastCharEnglish = isLastCharEnglish(i);
                boolean isSpecialChar = isSpecialChar(isLastCharEnglish, ch);
                float charHeight = getCharHeight(ch, isLastCharEnglish);
                if (h == 0 || i == 0) {
                    h = (int) ((isSpecialChar ? 3.0f : getNormalCharPaddingTop(3.0f)) + ((float) h));
                }
                h = (int) (((float) h) + charHeight);
                boolean isEnglishChar = isEnglishChar(ch);
                if (this.mCustomMaxHeight > 0) {
                    if (isEnglishChar) {
                        if (((float) h) + charHeight > ((float) this.mHeight) && ((float) h) + charHeight < ((float) this.mCustomMaxHeight)) {
                            this.mHeight = (int) ((((float) h) + charHeight) + 1.0f);
                        }
                    } else if (h > this.mHeight && h < this.mCustomMaxHeight) {
                        this.mHeight = h + 1;
                    }
                }
                if ((i <= 0 || !isEnglishChar || ((float) h) + charHeight <= ((float) this.mHeight)) && h <= this.mHeight) {
                    if (isSpecialChar) {
                        h += this.ENGLISH_CHAR_PADDING;
                    } else if (ch != ' ') {
                        h += this.mNormalCharPadding;
                    }
                    if (i == this.mText.length() - 1 && h > this.mMaxHeight) {
                        this.mMaxHeight = h;
                    }
                } else {
                    if (((float) h) > ((float) this.mMaxHeight) + charHeight) {
                        this.mMaxHeight = (int) (((float) h) - charHeight);
                    }
                    if (this.mMaxLines == 0 || realLine != this.mMaxLines) {
                        realLine++;
                        lineSpaceCount++;
                        i--;
                        h = 0;
                    } else if (this.mNeedCollipseEnd) {
                        this.mLastCanShowCharIndex = i - 1;
                    } else {
                        this.mLastCanShowCharIndex = i;
                    }
                }
            }
            i++;
        }
        if (this.mOnRealLineChangeListener != null) {
            this.mOnRealLineChangeListener.realLineChange(realLine);
        }
        int i2 = (this.mLineSpace * lineSpaceCount) + (this.mLineWidth * realLine);
        if (this.mNeedLeftLine) {
            dip2px = dip2px(getContext(), 10.0f);
        } else {
            dip2px = 0;
        }
        return dip2px + i2;
    }

    private float getCharHeight(char ch, boolean isLastCharEnglish) {
        boolean isEnglishChar = isSpecialChar(isLastCharEnglish, ch);
        if (ch == ' ') {
            return (float) dip2px(getContext(), 10.0f);
        }
        if (!isEnglishChar) {
            return (float) this.mFontHeight;
        }
        float[] space = new float[1];
        this.mPaint.getTextWidths(ch + "", space);
        return space[0];
    }

    private boolean isSpecialChar(boolean lastCharIsEnglish, char ch) {
        return (ch >= 'a' && ch <= 'z') || ((ch >= 'A' && ch <= 'Z') || ((lastCharIsEnglish && this.mTransAfterEngChars.contains(ch + "")) || this.mAlwaysTransChars.contains(ch + ""))
                || ((int)ch > 127 && (int)ch < 256));
    }

    private boolean isEnglishChar(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private void measureLineWidth() {
        if (this.mLineWidth == 0) {
            float[] widths = new float[1];
            this.mPaint.getTextWidths("正", widths);
            this.mLineWidth = (int) Math.ceil((double) widths[0]);
        }
    }

    private void measureFontHeight() {
        FontMetrics fm = this.mPaint.getFontMetrics();
        this.mFontHeight = (int) Math.ceil((double) (fm.bottom - fm.top));
        this.mFontBaselinePadding = fm.bottom;
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(1, dpValue, context.getResources().getDisplayMetrics());
    }
}
