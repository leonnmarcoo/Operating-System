import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

public class DEVELALEONMARCO5 extends JFrame {
    private JComboBox<Integer> numProcessComboBox;
    private JPanel inputPanel;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JLabel avgTATLabel;
    private JLabel avgWTLabel;
    private JTextArea ganttChartArea;
    private ArrayList<ProcessData> processes;
    private int numProcesses = 0;
    private int currentProcessInput = 0;

    public DEVELALEONMARCO5() {
        initializeGUI();
        processes = new ArrayList<>();
    }
    
    private void initializeGUI() {
        setTitle("HRRN SCHEDULING ALGORITHM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(51, 122, 183));
        JLabel titleLabel = new JLabel("HRRN SCHEDULING ALGORITHM", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(15, 10, 15, 10));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        
        createResultsPanel();
        mainPanel.add(createResultsPanel(), BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
    }
    
    private void createInputPanel() {
        inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(51, 122, 183), 2), 
            "Process Input", 
            0, 0, 
            new Font("Arial", Font.BOLD, 14), 
            new Color(51, 122, 183)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Enter no. of process:"), gbc);
        
        gbc.gridx = 1;
        numProcessComboBox = new JComboBox<>(new Integer[]{3, 4, 5});
        numProcessComboBox.setPreferredSize(new Dimension(100, 25));
        inputPanel.add(numProcessComboBox, gbc);
        
        gbc.gridx = 2;
        JButton startButton = new JButton("Start Input");
        startButton.setBackground(new Color(92, 184, 92));
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(_ -> startProcessInput());
        inputPanel.add(startButton, gbc);
        
        gbc.gridx = 3;
        JButton resetButton = new JButton("Try Again");
        resetButton.setBackground(new Color(217, 83, 79));
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(_ -> resetApplication());
        inputPanel.add(resetButton, gbc);
    }
    
    private JPanel createResultsPanel() {
        JPanel resultsPanel = new JPanel(new BorderLayout(10, 10));
        
        String[] columnNames = {"Process", "Arrival Time", "Burst Time", "Completion Time", "Turnaround Time", "Waiting Time", "Response Time"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        resultsTable = new JTable(tableModel);
        resultsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        resultsTable.setRowHeight(25);
        resultsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        resultsTable.getTableHeader().setBackground(new Color(51, 122, 183));
        resultsTable.getTableHeader().setForeground(Color.BLACK);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < resultsTable.getColumnCount(); i++) {
            resultsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane tableScrollPane = new JScrollPane(resultsTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Results Table"));
        resultsPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        avgTATLabel = new JLabel("Average TAT: 0.0", JLabel.CENTER);
        avgTATLabel.setFont(new Font("Arial", Font.BOLD, 14));
        avgTATLabel.setBorder(BorderFactory.createLineBorder(new Color(51, 122, 183), 2));
        avgTATLabel.setOpaque(true);
        avgTATLabel.setBackground(new Color(240, 248, 255));
        
        avgWTLabel = new JLabel("Average WT: 0.0", JLabel.CENTER);
        avgWTLabel.setFont(new Font("Arial", Font.BOLD, 14));
        avgWTLabel.setBorder(BorderFactory.createLineBorder(new Color(51, 122, 183), 2));
        avgWTLabel.setOpaque(true);
        avgWTLabel.setBackground(new Color(240, 248, 255));
        
        statsPanel.add(avgTATLabel);
        statsPanel.add(avgWTLabel);
        resultsPanel.add(statsPanel, BorderLayout.SOUTH);
        
        ganttChartArea = new JTextArea(4, 50);
        ganttChartArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        ganttChartArea.setEditable(false);
        ganttChartArea.setBackground(new Color(248, 249, 250));
        JScrollPane ganttScrollPane = new JScrollPane(ganttChartArea);
        ganttScrollPane.setBorder(BorderFactory.createTitledBorder("Gantt Chart"));
        resultsPanel.add(ganttScrollPane, BorderLayout.EAST);
        
        return resultsPanel;
    }
    
    private void startProcessInput() {
        numProcesses = (Integer) numProcessComboBox.getSelectedItem();
        
        processes.clear();
        currentProcessInput = 0;
        tableModel.setRowCount(0);
        avgTATLabel.setText("Average TAT: 0.0");
        avgWTLabel.setText("Average WT: 0.0");
        ganttChartArea.setText("");
        
        inputNextProcess();
    }
    
    private void inputNextProcess() {
        if (currentProcessInput < numProcesses) {
            String processName = "P" + (currentProcessInput + 1);
            
            JPanel processInputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            processInputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            
            JTextField arrivalField = new JTextField();
            JTextField burstField = new JTextField();
            
            processInputPanel.add(new JLabel("Arrival Time for " + processName + ":"));
            processInputPanel.add(arrivalField);
            processInputPanel.add(new JLabel("Burst Time for " + processName + ":"));
            processInputPanel.add(burstField);
            
            int result = JOptionPane.showConfirmDialog(this, processInputPanel, 
                "Enter Process " + processName + " Details", JOptionPane.OK_CANCEL_OPTION);
            
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int arrivalTime = Integer.parseInt(arrivalField.getText().trim());
                    int burstTime = Integer.parseInt(burstField.getText().trim());
                    
                    if (arrivalTime < 0 || burstTime <= 0) {
                        JOptionPane.showMessageDialog(this, "Arrival time must be non-negative and burst time must be positive.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        inputNextProcess(); // Retry current process
                        return;
                    }
                    
                    processes.add(new ProcessData(processName, arrivalTime, burstTime));
                    currentProcessInput++;
                    
                    if (currentProcessInput < numProcesses) {
                        inputNextProcess();
                    } else {
                        calculateAndDisplayResults();
                    }
                    
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter valid integers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    inputNextProcess(); // Retry current process
                }
            }
        }
    }
    
    private void calculateAndDisplayResults() {
        // Create a copy of processes to work with
        ArrayList<ProcessData> remainingProcesses = new ArrayList<>(processes);
        ArrayList<ProcessData> executedProcesses = new ArrayList<>();
        
        // Find the earliest arrival time
        int currentTime = remainingProcesses.stream().mapToInt(p -> p.arrivalTime).min().orElse(0);
        
        // HRRN Algorithm
        while (!remainingProcesses.isEmpty()) {
            // Find processes that have arrived by current time
            ArrayList<ProcessData> availableProcesses = new ArrayList<>();
            for (ProcessData process : remainingProcesses) {
                if (process.arrivalTime <= currentTime) {
                    availableProcesses.add(process);
                }
            }
            
            // If no process has arrived yet, jump to the next arrival time
            if (availableProcesses.isEmpty()) {
                currentTime = remainingProcesses.stream().mapToInt(p -> p.arrivalTime).min().orElse(currentTime);
                continue;
            }
            
            // Calculate response ratio for each available process
            ProcessData selectedProcess = null;
            double highestResponseRatio = -1;
            
            for (ProcessData process : availableProcesses) {
                int waitingTime = currentTime - process.arrivalTime;
                double responseRatio = (double)(waitingTime + process.burstTime) / process.burstTime;
                
                if (responseRatio > highestResponseRatio) {
                    highestResponseRatio = responseRatio;
                    selectedProcess = process;
                }
            }
            
            // Execute the selected process
            if (selectedProcess != null) {
                selectedProcess.responseTime = currentTime - selectedProcess.arrivalTime;
                selectedProcess.completionTime = currentTime + selectedProcess.burstTime;
                selectedProcess.turnaroundTime = selectedProcess.completionTime - selectedProcess.arrivalTime;
                selectedProcess.waitingTime = selectedProcess.turnaroundTime - selectedProcess.burstTime;
                
                currentTime = selectedProcess.completionTime;
                executedProcesses.add(selectedProcess);
                remainingProcesses.remove(selectedProcess);
            }
        }
        
        // Display results in original order
        double totalTAT = 0, totalWT = 0;
        for (ProcessData process : processes) {
            tableModel.addRow(new Object[]{
                process.name, process.arrivalTime, process.burstTime,
                process.completionTime, process.turnaroundTime, process.waitingTime, process.responseTime
            });
            totalTAT += process.turnaroundTime;
            totalWT += process.waitingTime;
        }
        
        double avgTAT = totalTAT / numProcesses;
        double avgWT = totalWT / numProcesses;
        avgTATLabel.setText(String.format("Average TAT: %.1f", avgTAT));
        avgWTLabel.setText(String.format("Average WT: %.1f", avgWT));
        
        generateGanttChart(executedProcesses.toArray(new ProcessData[0]));
    }
    
    private void generateGanttChart(ProcessData[] executedProcesses) {
        StringBuilder ganttChart = new StringBuilder();
        StringBuilder timelineNumbers = new StringBuilder();
        
        // Find the minimum arrival time
        int minArrivalTime = Integer.MAX_VALUE;
        for (ProcessData process : executedProcesses) {
            if (process.arrivalTime < minArrivalTime) {
                minArrivalTime = process.arrivalTime;
            }
        }
        
        ganttChart.append("Gantt Chart: |");
        
        // Add empty space from 0 to first arrival if needed
        if (minArrivalTime > 0) {
            ganttChart.append("   |");
        }
        
        // Add executed processes in their execution order
        for (ProcessData process : executedProcesses) {
            ganttChart.append(" ").append(process.name).append(" |");
        }
        ganttChart.append("\n");
        
        // Build timeline with proper alignment
        timelineNumbers.append("             ");
        
        // Start timeline from 0
        timelineNumbers.append("0");
        
        // Add idle time if processes don't start at 0
        if (minArrivalTime > 0) {
            // Add spacing to align under the separator after empty block
            timelineNumbers.append("    ").append(minArrivalTime);
        }
        
        // Add execution times for each process
        for (ProcessData process : executedProcesses) {
            // Add spacing to align under the separator after each process block
            timelineNumbers.append("   ").append(process.completionTime);
        }
        
        ganttChart.append(timelineNumbers.toString());
        ganttChartArea.setText(ganttChart.toString());
    }
    
    private void resetApplication() {
        numProcessComboBox.setSelectedIndex(0);
        processes.clear();
        currentProcessInput = 0;
        numProcesses = 0;
        tableModel.setRowCount(0);
        avgTATLabel.setText("Average TAT: 0.0");
        avgWTLabel.setText("Average WT: 0.0");
        ganttChartArea.setText("");
    }
    
    static class ProcessData {
        String name;
        int arrivalTime;
        int burstTime;
        int completionTime;
        int turnaroundTime;
        int waitingTime;
        int responseTime;
        
        ProcessData(String name, int arrivalTime, int burstTime) {
            this.name = name;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DEVELALEONMARCO5().setVisible(true);
        });
    }
}