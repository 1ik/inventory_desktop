/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import com.google.gson.Gson;
import httpClient.HttpRequest;
import httpClient.HttpResponse;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import models.Expense_model;
import models.Expense_type_model;
import models.Item_model;
import models.Memo_model;
import models.Return_model;
import models.Transfer_model;
import models.showroom_model;
import org.apache.log4j.helpers.DateTimeDateFormat;
import structures.Expense;
import structures.Expense_type;
import structures.Transfer;
import sync.Expense_sync;
import sync.Expense_type_sync;
import sync.Returns_sync;
import views.Expense_type_view;
import views.Item_table_view;
import views.Transfer_tab_view;




import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import structures.Item;
import structures.Return;
import structures.Showroom;
import structures.changed_item;
import sync.Memo_sync;
import sync.Transfer_sync;
import views.Return_table_item_view;
import views.initializing_form;



/**
 *
 * @author Anik
 */
public class Launcher extends javax.swing.JFrame {
    
    private static final int SELL_TAB_INDEX = 0;
    private static final int EXPENSE_TAB_INDEX = 1;
    private static final int ITEMS_TAB_INDEX = 2;
    private static final int RETURN_ITEMS_TAB_INDEX = 3;
    
    private static final String UPDATES_DOWNLOAD_JOB_GRUP = "updates_download";
    private initializing_form init_form;

    /**
     * Creates new form Launcher
     */
    public Launcher() {
        
        initComponents();
        Expense_type_view.reloadView(expense_type);
        Expense_sync.startsynching(15000); //15 seconds.
        init_transfer_table_onItemSelectedHandler();
        //init_new_transfer_check_job(); <- Transfer check job is not done periodically.
        init_new_return_check_job();
        init_new_memo_sync_job();
        init_transfer_showroom_list();
        
        showroom_label.setText(config.Config.SHOWROOM_NAME);
        
        DefaultTableModel model = (DefaultTableModel) sell_item_table.getModel();
        model.setRowCount(0);
        model = (DefaultTableModel) return_items_table.getModel();
        model.setRowCount(0);
        model = (DefaultTableModel) change_item_table.getModel();
        model.setRowCount(0);
    }
    
    
    
    
    
    ////////////////////////// TRIGGERS & HANDLERS ////////////////////////////////////////////
    private void init_transfer_table_onItemSelectedHandler(){
        //adding item on select listener on transfertable
        //so that when an transfer is selected we can see all the items under that transferid.
        transfers_table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                
                if(!lse.getValueIsAdjusting()){
                    int selectedRow = transfers_table.getSelectedRow();
                    if(selectedRow >= 0){
                        int transfer_id = (int) transfers_table.getValueAt(selectedRow, 0);
                        Item_table_view.load_table(items_table, item_summery_table ,Item_model.get(transfer_id));
                    }
                }   
            }
        });
    }
    
    /*
    * We no longer need this method as we are not doing transfersync periodically.
    * Transfer sync from now on will be Triggered on check button click.
    private void init_new_transfer_check_job(){
        try{
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            
            JobDetail job = newJob(Transfer_sync.class)
                            .withIdentity("TransferSync", UPDATES_DOWNLOAD_JOB_GRUP)
                            .build();
            Trigger trigger = newTrigger().withIdentity("myTrigger", UPDATES_DOWNLOAD_JOB_GRUP).startNow()
                                .withSchedule(simpleSchedule().withIntervalInSeconds(10).repeatForever()).build();
            scheduler.scheduleJob(job, trigger);
            
        }catch(SchedulerException e){
            
            e.printStackTrace();
        }
    }
    */
    
    private void init_new_return_check_job(){
         try{
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            
            JobDetail job = newJob(Returns_sync.class)
                            .withIdentity("ReturnSync", UPDATES_DOWNLOAD_JOB_GRUP)
                            .build();
            Trigger trigger = newTrigger().withIdentity("returnTrigger", UPDATES_DOWNLOAD_JOB_GRUP).startNow()
                                .withSchedule(simpleSchedule().withIntervalInSeconds(75).repeatForever()).build();
            scheduler.scheduleJob(job, trigger);
            
        }catch(SchedulerException e){
            e.printStackTrace();
        }
    }
    
    
    private void init_new_memo_sync_job(){
        try{
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            
            JobDetail job = newJob(Memo_sync.class)
                            .withIdentity("Memo_sync", UPDATES_DOWNLOAD_JOB_GRUP)
                            .build();
            Trigger trigger = newTrigger().withIdentity("memoTrigger", UPDATES_DOWNLOAD_JOB_GRUP).startNow()
                                .withSchedule(simpleSchedule().withIntervalInSeconds(45).repeatForever()).build();
            scheduler.scheduleJob(job, trigger);
            
        }catch(SchedulerException e){
            System.err.println("exception occured in Launcher : init_new_memo_sync_job() ");
            e.printStackTrace();
        }
    }
    
    
    /**
     * Loads the dropdown menu of showroom list in the transfer tab. from database.
     */
    private void init_transfer_showroom_list() {
        transfer_showroom_list.removeAllItems();
        ArrayList<Showroom> showrooms = showroom_model.get();
        for(Showroom room : showrooms) {
            if(room.getShowroom_id() != config.Config.SHOWROOM_ID) {
                transfer_showroom_list.addItem(room.getName());
            }
        }
    }
    
    
    ////////////////////////// TRIGGERS & HANDLERS  ////////////////////////////////////////////

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        showroom_label = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        sell_total_items_added = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        sell_total_price = new javax.swing.JLabel();
        sell_paid_field = new javax.swing.JTextField();
        sell_return_amount_field = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        sell_item_paid_button = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        sell_item_table = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        change_item_table = new javax.swing.JTable();
        change_barcode_field = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        sell_item_table_clear_button = new javax.swing.JButton();
        memo_row_delete = new javax.swing.JButton();
        change_table_clear = new javax.swing.JButton();
        change_item_remove = new javax.swing.JButton();
        sell_barcode_field = new javax.swing.JTextField();
        change_table_status = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        expense_type = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        expense_type_reload = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        expense_amount_field = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        expense_explanation_field = new javax.swing.JTextArea();
        submit_expense = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        expense_list_table = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        expense_total_label = new javax.swing.JLabel();
        expense_remove_button = new javax.swing.JButton();
        expense_confirm = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        transfers_table = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        items_table = new javax.swing.JTable();
        item_summery_jscroll = new javax.swing.JScrollPane();
        item_summery_table = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        return_item_barcode_field = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        return_items_confirm = new javax.swing.JButton();
        return_items_added_label = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        return_item_table_delete_entry_button = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        return_items_table = new javax.swing.JTable();
        transfer_showroom_list = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("IMAGINARY FASHION");

        showroom_label.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        showroom_label.setText("Orkid Plaza Branch");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(showroom_label, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)))
                .addGap(486, 486, 486))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showroom_label)
                .addContainerGap(59, Short.MAX_VALUE))
        );

        tabs.setBackground(new java.awt.Color(255, 255, 255));
        tabs.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tab_state_changed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 56, Short.MAX_VALUE)
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        sell_total_items_added.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        sell_total_items_added.setText("0");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Total Items Added :");

        sell_total_price.setFont(new java.awt.Font("Consolas", 0, 120)); // NOI18N
        sell_total_price.setForeground(new java.awt.Color(255, 51, 51));
        sell_total_price.setText("000");
        sell_total_price.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        sell_paid_field.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        sell_paid_field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sell_paid_fieldKeyPressed(evt);
            }
        });

        sell_return_amount_field.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        sell_return_amount_field.setText("000");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setText("Paid         :");

        sell_item_paid_button.setText("Paid");
        sell_item_paid_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sell_item_paid_buttonActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setText("Return      :");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(sell_total_items_added, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel13)
                                .addGap(18, 18, 18)
                                .addComponent(sell_paid_field, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel14)
                                .addGap(18, 18, 18)
                                .addComponent(sell_return_amount_field)))
                        .addGap(0, 201, Short.MAX_VALUE))
                    .addComponent(sell_total_price, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sell_item_paid_button, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sell_total_items_added, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(sell_total_price)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sell_paid_field, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(sell_return_amount_field))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(sell_item_paid_button, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        sell_item_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "item_id", "item_type", "size", "item_price", "transfer_id", "color_code"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(sell_item_table);

        change_item_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "item_id", "item_type", "item_size", "price", "color", "sell showroom", "sell date"
            }
        ));
        jScrollPane5.setViewportView(change_item_table);

        change_barcode_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                change_barcode_fieldActionPerformed(evt);
            }
        });
        change_barcode_field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                change_barcode_button_keyPressed(evt);
            }
        });

        jLabel3.setText("SELL");

        jLabel16.setText("CHANGE");

        sell_item_table_clear_button.setText("clear table");
        sell_item_table_clear_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sell_item_table_clear_buttonActionPerformed(evt);
            }
        });

        memo_row_delete.setText("Remove");
        memo_row_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memo_row_deleteActionPerformed(evt);
            }
        });

        change_table_clear.setText("clear table");
        change_table_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                change_table_clearActionPerformed(evt);
            }
        });

        change_item_remove.setText("Remove");
        change_item_remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                change_item_removeActionPerformed(evt);
            }
        });

        sell_barcode_field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sell_barcode_fieldKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(memo_row_delete, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(sell_item_table_clear_button))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                            .addComponent(change_table_status, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                            .addComponent(change_item_remove, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(change_table_clear))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(change_barcode_field, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(sell_barcode_field, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(87, 87, 87)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sell_barcode_field, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sell_item_table_clear_button)
                            .addComponent(memo_row_delete))
                        .addGap(52, 52, 52)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(change_barcode_field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(change_table_clear)
                            .addComponent(change_item_remove)
                            .addComponent(change_table_status)))
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 374, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        tabs.addTab("Sell", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        expense_type.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("Expense Name : ");

        expense_type_reload.setText("Refresh");
        expense_type_reload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expense_type_reloadActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("Amount         :");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("Explanation    :");

        expense_explanation_field.setColumns(20);
        expense_explanation_field.setRows(5);
        jScrollPane2.setViewportView(expense_explanation_field);

        submit_expense.setText("Submit");
        submit_expense.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submit_expenseActionPerformed(evt);
            }
        });

        expense_list_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "reason", "amount", "explanation", "time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane7.setViewportView(expense_list_table);

        jLabel12.setText("Total Amount : ");

        expense_total_label.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        expense_total_label.setText("000");

        expense_remove_button.setText("Remove");
        expense_remove_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expense_remove_buttonActionPerformed(evt);
            }
        });

        expense_confirm.setText("Confirm");
        expense_confirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expense_confirmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(expense_type, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(expense_type_reload))
                    .addComponent(jLabel5)
                    .addComponent(jLabel7)
                    .addComponent(expense_amount_field, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(expense_remove_button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addGap(26, 26, 26)
                        .addComponent(expense_total_label, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 1170, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(expense_confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(submit_expense)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(expense_type_reload)
                            .addComponent(expense_type, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(expense_amount_field, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(expense_total_label)
                            .addComponent(expense_remove_button))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(expense_confirm))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(submit_expense)
                .addContainerGap(177, Short.MAX_VALUE))
        );

        tabs.addTab("Expense", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        transfers_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Transfer id", "items", "Created at"
            }
        ));
        jScrollPane3.setViewportView(transfers_table);

        items_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "item_id", "type", "size", "transfer_id", "price", "color code"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(items_table);

        item_summery_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Total Items", "", "", "", "Total Price", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Float.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        item_summery_table.setFocusable(false);
        item_summery_jscroll.setViewportView(item_summery_table);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 380, Short.MAX_VALUE)
                        .addComponent(item_summery_jscroll, javax.swing.GroupLayout.PREFERRED_SIZE, 1044, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(item_summery_jscroll, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("Check");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 69, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tabs.addTab("Items", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        return_item_barcode_field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                return_item_barcode_fieldKeyPressed(evt);
            }
        });

        jLabel9.setText("showrooms");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Transfer Items to showroom");

        return_items_confirm.setText("Confirm");
        return_items_confirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                return_items_confirmActionPerformed(evt);
            }
        });

        return_items_added_label.setText("0");

        jLabel11.setText("ITEMS ADDED : ");

        return_item_table_delete_entry_button.setText("Delete Entry");
        return_item_table_delete_entry_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                return_item_table_delete_entry_buttonActionPerformed(evt);
            }
        });

        return_items_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "item id", "type", "size", "price", "transfer id", "color code"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane6.setViewportView(return_items_table);

        transfer_showroom_list.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel15.setText("BARCODE");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(transfer_showroom_list, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(return_item_barcode_field, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 496, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(return_items_added_label, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(282, 282, 282)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(return_items_confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(return_item_table_delete_entry_button, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(31, 31, 31)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(1567, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(return_items_confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(return_item_barcode_field, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11)
                        .addComponent(return_items_added_label, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(return_item_table_delete_entry_button))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transfer_showroom_list, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(64, 64, 64)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(455, Short.MAX_VALUE)))
        );

        tabs.addTab("Transfer Items", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tabs, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void expense_type_reloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expense_type_reloadActionPerformed
        // TODO add your handling code here:
        Expense_type[] expense_types = Expense_type_sync.DownloadExpense_types();
        Expense_type_model.insert(expense_types);
        Expense_type_view.reloadView(this.expense_type);
    }//GEN-LAST:event_expense_type_reloadActionPerformed

    private void submit_expenseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submit_expenseActionPerformed
        // TODO add your handling code here:

        DefaultTableModel model = (DefaultTableModel) expense_list_table.getModel();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        model.addRow(new Object[] {expense_type.getSelectedItem().toString() , expense_amount_field.getText(), expense_explanation_field.getText(), format.format(new Date())});
        
        //increase the amount label.
        Double d = Double.parseDouble(expense_total_label.getText()) + Double.parseDouble(expense_amount_field.getText());
        expense_total_label.setText(d+"");
        expense_amount_field.setText("");
        expense_explanation_field.setText("");
    }//GEN-LAST:event_submit_expenseActionPerformed

    
    private void tab_state_changed(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tab_state_changed
        
        
        if(tabs.getSelectedIndex() == ITEMS_TAB_INDEX){
            
            
            //get the transfertable.
                ArrayList<Transfer> transfers = Transfer_model.get();
                if(transfers.size() > 0){
                    Transfer_tab_view.load_table(transfers_table, transfers);
                    DefaultTableModel model = (DefaultTableModel) transfers_table.getModel();

                    int transfer_id = (int) model.getValueAt(0, 0);
                    Item_table_view.load_table(items_table, item_summery_table ,  Item_model.get(transfer_id));
                    transfers_table.addRowSelectionInterval(0, 0);
                }
            
            
            
        }
        
    }//GEN-LAST:event_tab_state_changed

    private void return_item_barcode_fieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_return_item_barcode_fieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == 10){
            int item_id = Integer.parseInt(return_item_barcode_field.getText());
            return_item_barcode_field.setText("");
            Item item = Item_model.get_single_item(item_id);
            
            if(item!=null){
                Return_table_item_view.add_item(return_items_table, item);
                return_items_added_label.setText(return_items_table.getRowCount()+"");
            }
        }
        
    }//GEN-LAST:event_return_item_barcode_fieldKeyPressed

    private void return_item_table_delete_entry_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_return_item_table_delete_entry_buttonActionPerformed
        // TODO add your handling code here:
        
        int rowselected = return_items_table.getSelectedRow();
        if(rowselected >= 0){
            DefaultTableModel model = (DefaultTableModel) return_items_table.getModel();
            model.removeRow(rowselected);
            return_items_added_label.setText(return_items_table.getRowCount()+"");
        }
    }//GEN-LAST:event_return_item_table_delete_entry_buttonActionPerformed

    private void return_items_confirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_return_items_confirmActionPerformed
        // TODO add your handling code here:
        int items [] = Return_table_item_view.get_items_from_table(return_items_table);
        if(items.length > 0){
            
            String showroom_name = transfer_showroom_list.getSelectedItem().toString();
            int showroom_id = showroom_model.getId(showroom_name);
            
            String itemlist = "";
            for(int i=0; i< items.length; i++) {
                int id = items[i];
                itemlist = (i < (items.length -1)) ? itemlist + id + "," : itemlist + id;
            }
            System.out.println("return item list added : "+ itemlist);
            Return ret = new Return(showroom_id, itemlist);
            Return_model.add_return(ret);
            Item_model.delete(items);
 
            DefaultTableModel model = (DefaultTableModel) return_items_table.getModel();
            model.setRowCount(0);
            return_item_barcode_field.setText("");
            return_items_added_label.setText("0");
        }
    }//GEN-LAST:event_return_items_confirmActionPerformed

    private void sell_barcode_fieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sell_barcode_fieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == 10){
            
            String input = sell_barcode_field.getText();
            int id = 0;
            try{
                id = Integer.parseInt(input);
            }catch (Exception e ){
                //exception 
                return;
            }
            
            return_item_barcode_field.setText("");
            Item item = Item_model.get_single_item(id);
            
            if(item!=null){
                //we are using return item table's function. it does the job very easily.
                Return_table_item_view.add_item(sell_item_table, item);
                sell_total_items_added.setText(return_items_table.getRowCount()+"");
                recalculate_sell_priece();
            }
            sell_barcode_field.setText("");
        }
    }//GEN-LAST:event_sell_barcode_fieldKeyPressed

    private void sell_paid_fieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sell_paid_fieldKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == 10){
            String paid_string = sell_paid_field.getText();
            float paid = Float.parseFloat(paid_string);
            
            String total_string = sell_total_price.getText();
            float total = Float.parseFloat(total_string);
            
            float ret = Math.abs(paid - total);
            sell_return_amount_field.setText(ret + "");
        }
        
        
    }//GEN-LAST:event_sell_paid_fieldKeyPressed

    private void sell_item_paid_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sell_item_paid_buttonActionPerformed
        // TODO add your handling code here:
        
        
        // we get the sell item ids.
        DefaultTableModel model = (DefaultTableModel) sell_item_table.getModel();
        int rows = model.getRowCount();
        int [] items = new int[rows];
        String itms = "";
        for(int i=0; i< model.getRowCount(); i++){
            items[i] = (int) model.getValueAt(i, 0);
            itms += items[i];
            if(i < model.getRowCount() -1){
                itms += ",";
            }
        }
        
        //we get the change item ids.
        DefaultTableModel change_table_model = (DefaultTableModel) change_item_table.getModel();
        int change_table_rows = change_table_model.getRowCount();
        String change_items = "";
        for(int i=0; i < change_table_rows; ++i) {
            change_items += i < change_table_rows - 1 ? change_table_model.getValueAt(i, 0) + "," : change_table_model.getValueAt(i, 0);
        }
        
        //update the memo table.
        System.out.println("adding memo...");
        Memo_model.add_memo(itms , change_items);
        
        //now remove the items from items table??
        System.out.println("removing items from item table...");
        Item_model.delete(items);
        
        //clear the table.
        System.out.println("clearing purchase table views. .. ");
        model.setRowCount(0);
        change_table_model.setRowCount(0);
        change_barcode_field.setText("");
        sell_return_amount_field.setText("");
        sell_paid_field.setText("");
        sell_total_price.setText("000");
        sell_total_items_added.setText("0");
        
    }//GEN-LAST:event_sell_item_paid_buttonActionPerformed

    private void expense_remove_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expense_remove_buttonActionPerformed
        // TODO add your handling code here:
        int rowselected = expense_list_table.getSelectedRow();
        if(rowselected >= 0){
            DefaultTableModel model = (DefaultTableModel) expense_list_table.getModel();
            //take the amount the row contains.
            Double rowAmount = Double.parseDouble(model.getValueAt(rowselected, 1).toString());
            model.removeRow(rowselected);
            
            //we have to deduct the amount of this row from total amount.
            Double amount = Double.parseDouble(expense_total_label.getText()) - rowAmount;
            expense_total_label.setText(amount+"");
            //return_items_added_label.setText(return_items_table.getRowCount()+"");
        }
    }//GEN-LAST:event_expense_remove_buttonActionPerformed

    private void expense_confirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expense_confirmActionPerformed

        ArrayList<Expense> expenses = new ArrayList();
        ArrayList<Expense_type> expense_types = Expense_type_model.get_expense_types();
        
        DefaultTableModel model = (DefaultTableModel) expense_list_table.getModel();
        int rowCount = model.getRowCount();
        for(int i=0; i< rowCount; ++i){
            int typeid = getTypeID(expense_types, model.getValueAt(i, 0).toString());
            String amoutnString = model.getValueAt(i, 1).toString();
            float amount = Float.parseFloat(amoutnString);
            String explanation = model.getValueAt(i, 2).toString();
            String dateTime = model.getValueAt(i, 3).toString();
            Expense expense = new Expense(typeid, amount, explanation, dateTime);
            expenses.add(expense);
        }
        
        Expense_model.add_expenses(expenses);
        model.setRowCount(0);
        expense_total_label.setText("00");
        expense_amount_field.setText("");
        expense_explanation_field.setText("");
                
    }//GEN-LAST:event_expense_confirmActionPerformed

    private void memo_row_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memo_row_deleteActionPerformed

        int selectedRow = sell_item_table.getSelectedRow();
        if(selectedRow >= 0) {
            DefaultTableModel tableModel = (DefaultTableModel) sell_item_table.getModel();
            tableModel.removeRow(selectedRow);
            recalculate_sell_priece();
        }
    }//GEN-LAST:event_memo_row_deleteActionPerformed

    private void sell_item_table_clear_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sell_item_table_clear_buttonActionPerformed
        
        //clear the table.
        DefaultTableModel model = (DefaultTableModel) sell_item_table.getModel();
        model.setRowCount(0);
        
        //clear all the labels.
        sell_total_items_added.setText("00");
        sell_total_price.setText("000");
        sell_barcode_field.setText("");
        sell_return_amount_field.setText("00");
        sell_paid_field.setText("");
        
    }//GEN-LAST:event_sell_item_table_clear_buttonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        Transfer_sync.sync();
                
    }//GEN-LAST:event_jButton1ActionPerformed

    private void change_table_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_change_table_clearActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) change_item_table.getModel();
        model.setRowCount(0);
        recalculate_sell_priece();
    }//GEN-LAST:event_change_table_clearActionPerformed

    private void change_item_removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_change_item_removeActionPerformed
        // TODO add your handling code here:
        int selectedRow = change_item_table.getSelectedRow();
        if(selectedRow >= 0) {
            DefaultTableModel tableModel = (DefaultTableModel) change_item_table.getModel();
            tableModel.removeRow(selectedRow);
            recalculate_sell_priece();
        }
    }//GEN-LAST:event_change_item_removeActionPerformed

    private void change_barcode_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_change_barcode_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_change_barcode_fieldActionPerformed

    private void change_barcode_button_keyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_change_barcode_button_keyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == 10){
            String input = change_barcode_field.getText();
            int id = 0;
            try{
                id = Integer.parseInt(input);
            }catch (Exception e ){
                //exception 
                return;
            }
            
            change_barcode_field.setText("");
            try {
                change_table_status.setText("checking..");
                HttpRequest req = new HttpRequest(new URL(config.Config.BASE_URL+"api/check_sold_item/"+id));
                HttpResponse resp = req.get();
                String json_response = resp.getReponse();
                if(json_response.length() <= 0) {
                    change_table_status.setText("Item not found.");
                    System.err.println("Change item not found");
                } else {
                    change_table_status.setText("Item found.");
                    System.out.println(json_response);
                    changed_item item = new Gson().fromJson(json_response, changed_item.class);
                    //System.out.println(item);
                    //now get the change table and insert this bitch into that table and ur done.
                    DefaultTableModel model = (DefaultTableModel) change_item_table.getModel();
                    model.addRow(new Object[]{
                        item.getItem_id(), 
                        item.getItem_type(), 
                        item.getItem_size(), 
                        item.getPrice(), 
                        item.getColor(), 
                        item.getSell_showroom(), 
                        item.getSell_date()});   
                    recalculate_sell_priece();
                }
            } catch (IOException ex) {
                Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }//GEN-LAST:event_change_barcode_button_keyPressed

    private int getTypeID(ArrayList<Expense_type> expenseTypes , String reasonname){
        int typeid = -1;
        for(Expense_type type : expenseTypes){
            if(type.getReason().equals(reasonname)){
                typeid = type.getId();
            }
        }
        return typeid;
    }
    
    
    
    public void recalculate_sell_priece(){
        DefaultTableModel model =  (DefaultTableModel) sell_item_table.getModel();
        int rowCount = model.getRowCount();
        
        float sell_price = 0;
        for(int i=0; i< rowCount; i++){
            String d =  model.getValueAt(i, 3) + "";
            System.out.println(d);
            sell_price += Float.parseFloat(d);
        }
        
        DefaultTableModel change_model =  (DefaultTableModel) change_item_table.getModel();
        int ChangerowCount = change_model.getRowCount();
        
        float change_price = 0;
        
        for(int i=0; i< ChangerowCount; i++){
            String d =  change_model.getValueAt(i, 3) + "";
            System.out.println(d);
            change_price += Float.parseFloat(d);
        }
        
        float price = sell_price - change_price;
        sell_total_items_added.setText((rowCount+ChangerowCount) + "");
        sell_total_price.setText(price + "");
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Launcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Launcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Launcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Launcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Launcher().setVisible(true);
            }
        });
        
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField change_barcode_field;
    private javax.swing.JButton change_item_remove;
    private javax.swing.JTable change_item_table;
    private javax.swing.JButton change_table_clear;
    private javax.swing.JLabel change_table_status;
    private javax.swing.JTextField expense_amount_field;
    private javax.swing.JButton expense_confirm;
    private javax.swing.JTextArea expense_explanation_field;
    private javax.swing.JTable expense_list_table;
    private javax.swing.JButton expense_remove_button;
    private javax.swing.JLabel expense_total_label;
    private javax.swing.JComboBox expense_type;
    private javax.swing.JButton expense_type_reload;
    private javax.swing.JScrollPane item_summery_jscroll;
    private javax.swing.JTable item_summery_table;
    private javax.swing.JTable items_table;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JButton memo_row_delete;
    private javax.swing.JTextField return_item_barcode_field;
    private javax.swing.JButton return_item_table_delete_entry_button;
    private javax.swing.JLabel return_items_added_label;
    private javax.swing.JButton return_items_confirm;
    private javax.swing.JTable return_items_table;
    private javax.swing.JTextField sell_barcode_field;
    private javax.swing.JButton sell_item_paid_button;
    private javax.swing.JTable sell_item_table;
    private javax.swing.JButton sell_item_table_clear_button;
    private javax.swing.JTextField sell_paid_field;
    private javax.swing.JLabel sell_return_amount_field;
    private javax.swing.JLabel sell_total_items_added;
    private javax.swing.JLabel sell_total_price;
    private javax.swing.JLabel showroom_label;
    private javax.swing.JButton submit_expense;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JComboBox transfer_showroom_list;
    private javax.swing.JTable transfers_table;
    // End of variables declaration//GEN-END:variables
}
