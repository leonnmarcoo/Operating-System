import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class DEVELALEONMARCO3 extends JFrame {
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
    private int timeQuantum = 2;

    public DEVELALEONMARCO3
() {
        initializeGUI();
        processes = new ArrayList<>();
    }
    
    private void initializeGUI() {
        setTitle("ROUND ROBIN SCHEDULING ALGORITHM");
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
        JLabel titleLabel = new JLabel("ROUND ROBIN SCHEDULING ALGORITHM", JLabel.CENTER);
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
        
        gbc.gridx = 2; gbc.gridy = 0;
        inputPanel.add(new JLabel("Time Quantum:"), gbc);
        
        gbc.gridx = 3;
        JComboBox<Integer> timeQuantumComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        timeQuantumComboBox.setSelectedItem(2);
        timeQuantumComboBox.setPreferredSize(new Dimension(100, 25));
        timeQuantumComboBox.addActionListener(evt -> timeQuantum = (Integer) timeQuantumComboBox.getSelectedItem());
        inputPanel.add(timeQuantumComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JButton startButton = new JButton("Start Input");
        startButton.setBackground(new Color(92, 184, 92));
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(evt -> startProcessInput());
        inputPanel.add(startButton, gbc);
        
        gbc.gridx = 2; gbc.gridwidth = 2;
        JButton resetButton = new JButton("Try Again");
        resetButton.setBackground(new Color(217, 83, 79));
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(evt -> resetApplication());
        inputPanel.add(resetButton, gbc);
    }
    
    private JPanel createResultsPanel() {
        JPanel resultsPanel = new JPanel(new BorderLayout(10, 10));
        
        String[] columnNames = {"Process", "Arrival Time", "Burst Time", "Completion Time", "Waiting Time", "Turnaround Time"};
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
        // Round Robin Scheduling Algorithm - cycles through processes in order
        ArrayList<String> executionOrder = new ArrayList<>();
        ArrayList<Integer> executionTimes = new ArrayList<>();
        
        // Create copies of processes to work with and sort by process name (P1, P2, P3...)
        ArrayList<ProcessData> processCopies = new ArrayList<>();
        for (ProcessData p : processes) {
            ProcessData copy = new ProcessData(p.name, p.arrivalTime, p.burstTime);
            processCopies.add(copy);
        }
        
        // Sort by process name to ensure P1, P2, P3 order
        processCopies.sort(Comparator.comparing(p -> p.name));
        
        int currentTime = 0;
        boolean allProcessesComplete = false;
        
        // Keep cycling through processes until all are complete
        while (!allProcessesComplete) {
            allProcessesComplete = true;
            
            // Cycle through each process in order
            for (ProcessData process : processCopies) {
                if (process.remainingTime > 0) {
                    allProcessesComplete = false;
                    
                    // Execute for time quantum or remaining time, whichever is smaller
                    int executionTime = Math.min(timeQuantum, process.remainingTime);
                    
                    executionOrder.add(process.name);
                    executionTimes.add(currentTime);
                    
                    currentTime += executionTime;
                    process.remainingTime -= executionTime;
                    
                    // If process is complete, set completion time
                    if (process.remainingTime == 0) {
                        process.completionTime = currentTime;
                        process.turnaroundTime = process.completionTime - process.arrivalTime;
                        process.waitingTime = process.turnaroundTime - process.burstTime;
                        
                        // Update the original process in the processes list
                        for (ProcessData original : processes) {
                            if (original.name.equals(process.name)) {
                                original.completionTime = process.completionTime;
                                original.turnaroundTime = process.turnaroundTime;
                                original.waitingTime = process.waitingTime;
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        // Add final time for Gantt chart
        executionTimes.add(currentTime);
        
        // Display results in table (sort by process name for consistent display)
        processes.sort(Comparator.comparing(p -> p.name));
        
        double totalWT = 0, totalTAT = 0;
        for (ProcessData process : processes) {
            tableModel.addRow(new Object[]{
                process.name, process.arrivalTime, process.burstTime,
                process.completionTime, process.waitingTime, process.turnaroundTime
            });
            totalWT += process.waitingTime;
            totalTAT += process.turnaroundTime;
        }
        
        double avgWT = totalWT / numProcesses;
        double avgTAT = totalTAT / numProcesses;
        avgTATLabel.setText(String.format("Average TAT: %.1f", avgTAT));
        avgWTLabel.setText(String.format("Average WT: %.1f", avgWT));
        
        generateRoundRobinGanttChart(executionOrder, executionTimes);
    }
    
    private void generateRoundRobinGanttChart(ArrayList<String> executionOrder, ArrayList<Integer> executionTimes) {
        StringBuilder ganttChart = new StringBuilder();
        StringBuilder timelineNumbers = new StringBuilder();
        
        ganttChart.append("Gantt Chart: |");
        for (String processName : executionOrder) {
            ganttChart.append(" ").append(processName).append(" |");
        }
        ganttChart.append("\n");
        
        timelineNumbers.append("             ");
        for (int time : executionTimes) {
            timelineNumbers.append(String.format("%-5s", time));
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
        int remainingTime; // For Round Robin scheduling
        int completionTime;
        int waitingTime;
        int turnaroundTime;
        
        ProcessData(String name, int arrivalTime, int burstTime) {
            this.name = name;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.remainingTime = burstTime; // Initialize remaining time
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DEVELALEONMARCO3
        ().setVisible(true);
        });
    }
}