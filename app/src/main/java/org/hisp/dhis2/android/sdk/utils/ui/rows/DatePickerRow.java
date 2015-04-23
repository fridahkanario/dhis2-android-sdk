/*
 *  Copyright (c) 2015, University of Oslo
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, this
 *  * list of conditions and the following disclaimer.
 *  *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *  * this list of conditions and the following disclaimer in the documentation
 *  * and/or other materials provided with the distribution.
 *  * Neither the name of the HISP project nor the names of its contributors may
 *  * be used to endorse or promote products derived from this software without
 *  * specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.hisp.dhis2.android.sdk.utils.ui.rows;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.hisp.dhis2.android.sdk.R;
import org.hisp.dhis2.android.sdk.persistence.models.BaseValue;
import org.hisp.dhis2.android.sdk.utils.ui.DatePickerDialog;
import org.joda.time.LocalDate;

public class DatePickerRow implements Row {
    private LayoutInflater inflater;
    private BaseValue dataValue;
    private EditText editText;
    private Context context;
    private LocalDate currentDate;
    private boolean editable = true;
    DatePickerRowHolder holder;
    private String label;

    /**
     *
     * @param inflater
     * @param label
     * @param context
     * @param dataValue
     */
    public DatePickerRow(LayoutInflater inflater, String label, Context context, BaseValue dataValue) {
        this.inflater= inflater;
        this.label = label;
        this.context = context;
        this.dataValue = dataValue;
        
        currentDate = new LocalDate();
    }

    @Override
    public View getView(View convertView) {
        View view;
        
        if (convertView == null) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.listview_row_datepicker, null);
            
            TextView textLabel = (TextView) rootView.findViewById(R.id.text_label);
            ImageView clearButton = (ImageView) rootView.findViewById(R.id.clearEditText);
            EditText pickerInvoker = (EditText) rootView.findViewById(R.id.date_picker_dialog_invoker);
            editText = pickerInvoker;
          
            DateSetListener dateSetListener = new DateSetListener(dataValue, editText);
            OnEditTextClickListener invokerListener = new OnEditTextClickListener(
                    label, currentDate, dateSetListener, context);
            ClearButtonListener clButtonListener = new ClearButtonListener(pickerInvoker, dataValue);
            
            pickerInvoker.setOnClickListener(invokerListener);
            clearButton.setOnClickListener(clButtonListener);
            
            holder = new DatePickerRowHolder(textLabel, pickerInvoker, clearButton,
                    clButtonListener, dateSetListener, invokerListener);
            
            rootView.setTag(holder);
            
            view = rootView; 
        } else {
            view = convertView;
            holder = (DatePickerRowHolder) view.getTag();  
        }
        
        holder.textLabel.setText(label);
        
        holder.dateSetListener.setDataValue(dataValue);
        holder.invokerListener.setFieldAndListener(label, holder.dateSetListener);
        holder.pickerInvoker.setText(dataValue.value);
        holder.pickerInvoker.setOnClickListener(holder.invokerListener);
        
        holder.cbListener.setEditText(holder.pickerInvoker, dataValue);
        holder.clearButton.setOnClickListener(holder.cbListener);
        setEditable(editable);

        return view;
    }

    @Override
    public TextView getEntryView() {
        return editText;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
        if(holder != null) {
            if(editable) {
                holder.pickerInvoker.setEnabled(true);
                holder.clearButton.setEnabled(true);
            } else {
                holder.pickerInvoker.setEnabled(false);
                holder.clearButton.setEnabled(false);
            }
        }
    }


    private class DatePickerRowHolder {
        final TextView textLabel;
        final EditText pickerInvoker;
        final DateSetListener dateSetListener;
        final OnEditTextClickListener invokerListener;
        final ImageView clearButton;
        final ClearButtonListener cbListener;
        
        DatePickerRowHolder(TextView textLabel, EditText pickerInvoker,
                ImageView clearButton, ClearButtonListener cbListener,
                DateSetListener dateSetListener, OnEditTextClickListener invokerListener) {
            
            this.textLabel = textLabel;
            this.pickerInvoker = pickerInvoker;
            this.dateSetListener = dateSetListener;
            this.invokerListener = invokerListener;
            this.clearButton = clearButton;
            this.cbListener = cbListener;
        }
    }
    
    private class DateSetListener implements DatePickerDialog.OnDateSetListener {
        private BaseValue dataValue;
        private EditText editText;
    
        DateSetListener(BaseValue dataValue, EditText editText) {
            this.dataValue = dataValue;
            this.editText = editText;
            if(dataValue != null && dataValue.value != null) {
                this.editText.setText(dataValue.value);
            }
        }
        
        void setDataValue(BaseValue dataValue) {
            this.dataValue = dataValue;
        }
        
        @Override
        public void onDateSet(LocalDate date) {
            dataValue.value = date.toString("YYYY-MM-dd");
            if(editText != null)
                editText.setText(dataValue.value);
        }
        
    }
    
    private class OnEditTextClickListener implements OnClickListener {
        private String label;
        private DateSetListener listener;
        private LocalDate currentDate;
        private Context context;
       
        OnEditTextClickListener(String label, LocalDate currentDate,
                DateSetListener listener, Context context) {
            this.currentDate = currentDate;
            
            this.label = label;
            this.listener = listener;
            this.context = context;
        }
        
        void setFieldAndListener(String label, DateSetListener listener) {
            this.label = label;
            this.listener = listener;
        } 
        
		@Override
        public void onClick(View view) {
            DatePickerDialog picker = new DatePickerDialog(context, listener, label,
                    currentDate.getYear(), currentDate.getMonthOfYear() - 1, currentDate.getDayOfMonth()); 
            picker.show();
        }   
    }
    
    private class ClearButtonListener implements OnClickListener {
        private EditText editText;
        private BaseValue dataValue;
        
        public ClearButtonListener(EditText editText, BaseValue dataValue) {
            this.editText = editText;
            this.dataValue = dataValue;
        }
        
        public void setEditText(EditText editText, BaseValue dataValue) {
            this.editText = editText;
            this.dataValue = dataValue;
        }

        @Override
        public void onClick(View view) {
            editText.setText(BaseValue.EMPTY_VALUE);
            dataValue.value = BaseValue.EMPTY_VALUE;
        }     
    }
}
