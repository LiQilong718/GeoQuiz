package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    //新增一个常量作为将要存储在bundle中的键值对的键
    private static final String KEY_INDEX = "index";
    //设置请求代码常量
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;
    private boolean mIsCheater;//新增一个成员变量用来保存CheatActivity回传的值


    /**
     * 使用upateQuestion()封装公共代码
     */
    private void updateQuestion() {
       /* Log.d(TAG, "Updating question text for question #" + mCurrentIndex,
                new Exception());//为updateQuestion()方法添加日志输出语句*/

        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    /**
     * 接收boolean类型的变量参数，判别用户单击了TRUE还是FALSE按钮，
     * 然后将用户的答案同当前Question对象中的答案作比较。
     * 最后判断答案正确与否，生成一个Toast消息反馈给用户
     */
    private void checkAnsewer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;

        } else {


            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle)called");
        setContentView(R.layout.activity_quiz);
        /**
         * * 引用TextView，并将其文本内容设置为当前数组索引所指向的地理知识问题。
         */
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        /*int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);*/
        /**
         * 正确按钮点击事件
         */
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Toast.makeText(QuizActivity.this, R.string.incorrect_toast,
                        Toast.LENGTH_SHORT).show();*/
                checkAnsewer(true);
            }
        });
        /**
         * 错误按钮点击事件
         */
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(QuizActivity.this, R.string.correct_toast,
                        Toast.LENGTH_SHORT).show();*/
                checkAnsewer(false);
            }
        });
        /**
         * 处理NEXT按钮，首先引用NEXT按钮，然后为其设置监听器View.OnClickListener.
         * 该监听器的作用是：递增数组索引并相应更新显示TextView的文本内容
         */
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                /*int question = mQuestionBank[mCurrentIndex].getTextResId();
                mQuestionTextView.setText(question);*/
                mIsCheater = false;
                updateQuestion();
            }
        });
        /**
         * 启用Cheat按钮 并设置点击事件
         */
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //启动CheatActivity
                // Intent i = new Intent(QuizActivity.this, CheatAvtivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatAvtivity.newIntent(QuizActivity.this, answerIsTrue);
                // startActivity(i);
                //从子Activity中获取返回信息
                startActivityForResult(i, REQUEST_CODE_CHEAT);

            }
        });
        /**
         * 判断Bundle中保存的数据是否为空
         */
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        updateQuestion();//更新问题
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatAvtivity.wasAnswerShown(data);
        }
    }

    /**
     * 覆盖onSaveInstanceState()方法，
     * 以新增的常量值“KEY_INDEX”为键，将mCurrentIndex变量值保存到bundle中
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()called");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()called");
    }

}
