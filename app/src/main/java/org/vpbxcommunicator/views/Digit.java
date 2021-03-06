package org.vpbxcommunicator.views;

/*
Digit.java
Copyright (C) 2017  Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.linphone.core.Core;
import org.linphone.core.tools.Log;
import org.vpbxcommunicator.LinphoneActivity;
import org.vpbxcommunicator.LinphoneManager;
import org.vpbxcommunicator.LinphoneService;
import org.vpbxcommunicator.R;
import org.vpbxcommunicator.call.CallActivity;
import org.vpbxcommunicator.settings.LinphonePreferences;

@SuppressLint("AppCompatCustomView")
public class Digit extends Button implements AddressAware {

    private AddressText mAddress;
    private boolean mPlayDtmf;
    private boolean isPressed = false;

    public Digit(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        setLongClickable(true);
    }

    public Digit(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLongClickable(true);
    }

    public Digit(Context context) {
        super(context);
        setLongClickable(true);
    }

    public void setAddressWidget(AddressText address) {
        mAddress = address;
    }

    public void setPlayDtmf(boolean play) {
        mPlayDtmf = play;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);

        if (text == null || text.length() < 1) {
            return;
        }

        DialKeyListener lListener = new DialKeyListener();
        setOnClickListener(lListener);
        setOnTouchListener(lListener);

        if ("0+".equals(text.toString())) {
            setOnLongClickListener(lListener);
        }

        if ("1".equals(text.toString())) {
            setOnLongClickListener(lListener);
        }
    }

    private class DialKeyListener implements OnClickListener, OnTouchListener, OnLongClickListener {
        final char mKeyCode;
        boolean mIsDtmfStarted;

        DialKeyListener() {
            mKeyCode = Digit.this.getText().subSequence(0, 1).charAt(0);
        }

        private boolean linphoneServiceReady() {
            if (!LinphoneService.isReady()) {
                Log.w("Service is not ready while pressing digit");
                Toast.makeText(
                                getContext(),
                                getContext().getString(R.string.skipable_error_service_not_ready),
                                Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
            return true;
        }

        public void onClick(View v) {
            if (mPlayDtmf) {
                if (!linphoneServiceReady()) return;
                Core lc = LinphoneManager.getLc();
                lc.stopDtmf();
                mIsDtmfStarted = false;
                if (lc.inCall()) {
                    lc.getCurrentCall().sendDtmf(mKeyCode);
                }
            }

            if (mAddress != null) {
                int lBegin = mAddress.getSelectionStart();
                if (lBegin == -1) {
                    lBegin = mAddress.length();
                }
                if (lBegin >= 0) {
                    mAddress.getEditableText().insert(lBegin, String.valueOf(mKeyCode));
                }

                if (LinphonePreferences.instance().getDebugPopupAddress() != null
                        && mAddress.getText()
                                .toString()
                                .equals(LinphonePreferences.instance().getDebugPopupAddress())) {
                    displayDebugPopup();
                }
            }
        }

        // this is used to set the background to pressed/the original
        // there is probably a better way of doing it but it works for the mean time
        private void digitBackgroundHelper(View view) {
            int id = view.getId();

            switch (id) {
                case R.id.Digit1:
                    setBackgroundResource(R.drawable.digit_1);
                    break;
                case R.id.Digit2:
                    setBackgroundResource(R.drawable.digit_2);
                    break;
                case R.id.Digit3:
                    setBackgroundResource(R.drawable.digit_3);
                    break;
                case R.id.Digit4:
                    setBackgroundResource(R.drawable.digit_4);
                    break;
                case R.id.Digit5:
                    setBackgroundResource(R.drawable.digit_5);
                    break;
                case R.id.Digit6:
                    setBackgroundResource(R.drawable.digit_6);
                    break;
                case R.id.Digit7:
                    setBackgroundResource(R.drawable.digit_7);
                    break;
                case R.id.Digit8:
                    setBackgroundResource(R.drawable.digit_8);
                    break;
                case R.id.Digit9:
                    setBackgroundResource(R.drawable.digit_9);
                    break;
                case R.id.Digit0:
                    setBackgroundResource(R.drawable.digit_zero);
                    break;
                case R.id.Digit_hashtag:
                    setBackgroundResource(R.drawable.digit_hashtag);
                    break;
                case R.id.Digit_asterisk:
                    setBackgroundResource(R.drawable.digit_asterisk);
                    break;
            }
        }

        private void digitOnClickHelper(View view) {
            int id = view.getId();

            switch (id) {
                case R.id.Digit1:
                    setBackgroundResource(R.drawable.digit_1_pressed);
                    break;

                case R.id.Digit2:
                    setBackgroundResource(R.drawable.digit_2_pressed);
                    break;

                case R.id.Digit3:
                    setBackgroundResource(R.drawable.digit_3_pressed);
                    break;

                case R.id.Digit4:
                    setBackgroundResource(R.drawable.digit_4_pressed);
                    break;

                case R.id.Digit5:
                    setBackgroundResource(R.drawable.digit_5_pressed);
                    break;

                case R.id.Digit6:
                    setBackgroundResource(R.drawable.digit_6_pressed);
                    break;

                case R.id.Digit7:
                    setBackgroundResource(R.drawable.digit_7_pressed);
                    break;

                case R.id.Digit8:
                    setBackgroundResource(R.drawable.digit_8_pressed);
                    break;

                case R.id.Digit9:
                    setBackgroundResource(R.drawable.digit_9_pressed);
                    break;

                case R.id.Digit0:
                    setBackgroundResource(R.drawable.digit_zero_pressed);
                    break;

                case R.id.Digit_asterisk:
                    setBackgroundResource(R.drawable.digit_asterisk_pressed);
                    break;

                case R.id.Digit_hashtag:
                    setBackgroundResource(R.drawable.digit_hashtag_pressed);
                    break;
            }
        }

        void displayDebugPopup() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle(getContext().getString(R.string.debug_popup_title));
            if (LinphonePreferences.instance().isDebugEnabled()) {
                alertDialog.setItems(
                        getContext().getResources().getStringArray(R.array.popup_send_log),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    LinphonePreferences.instance().setDebugEnabled(false);
                                }
                                if (which == 1) {
                                    Core lc = LinphoneManager.getLcIfManagerNotDestroyedOrNull();
                                    if (lc != null) {
                                        lc.uploadLogCollection();
                                    }
                                }
                            }
                        });

            } else {
                alertDialog.setItems(
                        getContext().getResources().getStringArray(R.array.popup_enable_log),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    LinphonePreferences.instance().setDebugEnabled(true);
                                }
                            }
                        });
            }
            alertDialog.show();
            mAddress.getEditableText().clear();
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (!mPlayDtmf) return false;
            if (!linphoneServiceReady()) return true;

            if (CallActivity.isInstanciated()) {
                CallActivity.instance().resetControlsHidingCallBack();
            }

            Core lc = LinphoneManager.getLc();
            if (event.getAction() == MotionEvent.ACTION_DOWN && !mIsDtmfStarted) {
                LinphoneManager.getInstance().playDtmf(getContext().getContentResolver(), mKeyCode);
                mIsDtmfStarted = true;
            } else {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    lc.stopDtmf();
                    mIsDtmfStarted = false;
                }
            }

            boolean isPressed = event.getAction() == MotionEvent.ACTION_DOWN;
            boolean isReleased = event.getAction() == MotionEvent.ACTION_UP;
            if (isPressed) {
                // setBackgroundResource(R.drawable.pressed_numeric_button);
                digitOnClickHelper(v);
            } else if (isReleased) {
                digitBackgroundHelper(v);
            }

            return false;
        }

        public boolean onLongClick(View v) {
            int id = v.getId();
            Core lc = LinphoneManager.getLc();

            if (mPlayDtmf) {
                if (!linphoneServiceReady()) return true;
                // Called if "0+" dtmf
                lc.stopDtmf();
            }

            if (id == R.id.Digit1 && lc.getCalls().length == 0) {
                String voiceMail = LinphonePreferences.instance().getVoiceMailUri();
                mAddress.getEditableText().clear();
                if (voiceMail == null) {
                    Toast.makeText(
                                    LinphoneActivity.instance(),
                                    "Set Up Voicemail URI in Call Settings",
                                    Toast.LENGTH_LONG)
                            .show();
                } else {
                    mAddress.getEditableText().append(voiceMail);
                    LinphoneManager.getInstance().newOutgoingCall(mAddress);
                }
                return true;
            }

            if (mAddress == null) return true;

            int lBegin = mAddress.getSelectionStart();
            if (lBegin == -1) {
                lBegin = mAddress.getEditableText().length();
            }
            if (lBegin >= 0) {
                mAddress.getEditableText().insert(lBegin, "+");
            }
            return true;
        }
    }
}
