package com.ma.customview.view;

import android.content.Context;

import android.text.Editable;
import android.text.NoCopySpan;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.ma.customview.R;

public class SpanEditText extends AppCompatEditText {

    private Context mContext;
    private OnKeyListener keyListener;

    public SpanEditText(Context context) {
        super(context);
        mContext=context;
        init();
    }

    public SpanEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        init();
    }

    public SpanEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        init();
    }

    private void init() {
        setEditableFactory(new NoCopySpanEditableFactory(new DirtySpanWatcher()));
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new MyInputConnection(super.onCreateInputConnection(outAttrs), true);
    }

    //设置监听回调
    public void setWatchDeleteEvent(OnKeyListener listener){
        keyListener = listener;
    }

    /**
     * 添加 []内容
     *
     * @param text 包含 [] 符号的字符
     */
    public void addSpan(String text) {
        getText().insert(getSelectionEnd(), text);
        DataSpan myTextSpan = new DataSpan(ContextCompat.getColor(mContext, R.color.blue));
        getText().setSpan(myTextSpan, getSelectionEnd() - text.length(), getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setSelection(getText().length());
    }

    public SpannableString subSpanString(String content) {
        SpannableString span=new SpannableString(content);
        if(content.contains("[测试]")){
            span=searchAllIndex("[测试]",span,content);
        }
        return span;
    }

    private SpannableString searchAllIndex(String spanString, SpannableString span, String content) {
        int index = content.indexOf(spanString);
        do{
            DataSpan myTextSpan = new DataSpan(ContextCompat.getColor(mContext,R.color.blue));
            span.setSpan(myTextSpan,index,index+spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            index = content.indexOf(spanString, index + 1);
        } while (index!= -1);
        return span;
    }



    /**
     * 找到最后 Span 块
     *
     * @param text
     * @return
     */
    public static boolean KeyDownHelper(Editable text) {
        int selectionEnd = Selection.getSelectionEnd(text);
        int selectionStart = Selection.getSelectionStart(text);
        DataSpan[] spans = text.getSpans(selectionStart, selectionEnd, DataSpan.class);
        for (DataSpan span : spans) {
            if (span != null) {
                int spanStart = text.getSpanStart(span);
                int spanEnd = text.getSpanEnd(span);
                if (selectionEnd == spanEnd) {
                    Selection.setSelection(text, spanStart, spanEnd);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 数据载体
     */
    class DataSpan extends ForegroundColorSpan {
        public DataSpan(int color) {
            super(color);
        }
    }

    private class MyInputConnection extends InputConnectionWrapper {

        public MyInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        //覆盖事件传递
        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (keyListener != null) {
                keyListener.onKey(SpanEditText.this,event.getKeyCode(),event);
            }
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            //在删除时，输入框无内容，或者删除以后输入框无内容
            if (beforeLength == 1 || afterLength == 0 || beforeLength == 0) {
                // backspace
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN , KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }

    }


    class NoCopySpanEditableFactory extends Editable.Factory {

        private NoCopySpan spans;

        public NoCopySpanEditableFactory(NoCopySpan spans) {
            this.spans = spans;
        }

        @Override
        public Editable newEditable(CharSequence source) {
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(source);
            stringBuilder.setSpan(spans, 0, source.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            return stringBuilder;
        }
    }

    class DirtySpanWatcher implements SpanWatcher {

        private int selStart = 0;
        private int selEnd = 0;

        @Override
        public void onSpanAdded(Spannable text, Object what, int start, int end) {

        }

        @Override
        public void onSpanRemoved(Spannable text, Object what, int start, int end) {

        }

        @Override
        public void onSpanChanged(final Spannable text, Object what, int ostart, int oend, int nstart, int nend) {
            if (what == Selection.SELECTION_END && selEnd != nstart) {
                selEnd = nstart;
                DataSpan[] spans = text.getSpans(nstart, nend, DataSpan.class);
                for (DataSpan span : spans) {
                    if (span != null) {
                        int spanStart = text.getSpanStart(span);
                        int spanEnd = text.getSpanEnd(span);
                        if (Math.abs(selEnd - spanEnd) > Math.abs(selEnd - spanStart)) {
                            Selection.setSelection(text, Selection.getSelectionStart(text), spanStart);
                        }else{
                            Selection.setSelection(text, Selection.getSelectionStart(text), spanEnd);
                        }
                    }
                }
            }

            if (what == Selection.SELECTION_START && selStart!= nstart) {
                selStart = nstart;
                DataSpan[] spans = text.getSpans(nstart, nend, DataSpan.class);
                for (DataSpan span : spans) {
                    if (span != null) {
                        int spanStart = text.getSpanStart(span);
                        int spanEnd = text.getSpanEnd(span);
                        if (Math.abs(selStart - spanEnd) > Math.abs(selStart - spanStart)) {
                            Selection.setSelection(text, spanStart, Selection.getSelectionEnd(text));
                        }else{
                            Selection.setSelection(text, spanEnd, Selection.getSelectionEnd(text));
                        }
                    }
                }
            }

            int spanEnd = text.getSpanEnd(what);
            int spanStart = text.getSpanStart(what);
            if (spanStart >= 0 && spanEnd >= 0 && what instanceof DataSpan) {
                CharSequence charSequence = text.subSequence(spanStart, spanEnd);
                if (!charSequence.toString().contains("[")) {
                    DataSpan[] spans = text.getSpans(spanStart, spanEnd, DataSpan.class);
                    for (DataSpan span : spans) {
                        if (span != null) {
                            text.removeSpan(span);
                            break;
                        }
                    }
                }
            }
        }
    }
}
