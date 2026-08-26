using System;
using System.Drawing;
using System.Windows.Forms;
using System.Diagnostics;

namespace LoginApp
{
    static class Program
    {
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new MainForm());
        }
    }

    public class MainForm : Form
    {
        private Panel loginPanel;
        private Panel loggedInPanel;
        private TextBox txtUsername;
        private TextBox txtPassword;
        private Label lblError;
        private Label lblUserDisplay;
        private Label lblUserEmail;

        public MainForm()
        {
            this.Text = "HCM Portal - Enterprise Sign In";
            this.Size = new Size(420, 680);
            this.StartPosition = FormStartPosition.CenterScreen;
            this.FormBorderStyle = FormBorderStyle.FixedDialog;
            this.MaximizeBox = false;
            this.BackColor = Color.FromArgb(245, 246, 248);

            InitializeLoginView();
        }

        private void InitializeLoginView()
        {
            loginPanel = new Panel
            {
                Dock = DockStyle.Fill,
                BackColor = Color.FromArgb(248, 249, 250)
            };

            // Top Hero Banner
            Panel headerPanel = new Panel
            {
                Dock = DockStyle.Top,
                Height = 110,
                BackColor = Color.FromArgb(26, 115, 232)
            };

            Label titleLabel = new Label
            {
                Text = "HCM Portal",
                ForeColor = Color.White,
                Font = new Font("Segoe UI", 18, FontStyle.Bold),
                Location = new Point(24, 24),
                AutoSize = true
            };

            Label subtitleLabel = new Label
            {
                Text = "Sign in to access your employee workspace",
                ForeColor = Color.FromArgb(220, 235, 252),
                Font = new Font("Segoe UI", 9.5f),
                Location = new Point(24, 62),
                AutoSize = true
            };

            headerPanel.Controls.Add(titleLabel);
            headerPanel.Controls.Add(subtitleLabel);

            // Body Container
            Panel body = new Panel
            {
                Location = new Point(24, 126),
                Size = new Size(356, 500),
                BackColor = Color.White,
                BorderStyle = BorderStyle.FixedSingle
            };

            Label lblSignIn = new Label
            {
                Text = "Account Sign In",
                Font = new Font("Segoe UI", 13, FontStyle.Bold),
                ForeColor = Color.FromArgb(32, 33, 36),
                Location = new Point(20, 16),
                AutoSize = true
            };

            // Username
            Label lblUser = new Label
            {
                Text = "Username or Email",
                Font = new Font("Segoe UI", 9, FontStyle.Bold),
                ForeColor = Color.FromArgb(95, 99, 104),
                Location = new Point(20, 56),
                AutoSize = true
            };

            txtUsername = new TextBox
            {
                Location = new Point(20, 78),
                Size = new Size(314, 28),
                Font = new Font("Segoe UI", 10.5f),
                Text = "admin"
            };

            // Password
            Label lblPass = new Label
            {
                Text = "Password",
                Font = new Font("Segoe UI", 9, FontStyle.Bold),
                ForeColor = Color.FromArgb(95, 99, 104),
                Location = new Point(20, 116),
                AutoSize = true
            };

            txtPassword = new TextBox
            {
                Location = new Point(20, 138),
                Size = new Size(314, 28),
                Font = new Font("Segoe UI", 10.5f),
                PasswordChar = '•',
                Text = "admin123"
            };

            lblError = new Label
            {
                ForeColor = Color.Red,
                Location = new Point(20, 172),
                Size = new Size(314, 20),
                Font = new Font("Segoe UI", 8.5f),
                Visible = false
            };

            // Sign In Button
            Button btnLogin = new Button
            {
                Text = "Sign In",
                Location = new Point(20, 196),
                Size = new Size(314, 40),
                BackColor = Color.FromArgb(26, 115, 232),
                ForeColor = Color.White,
                Font = new Font("Segoe UI", 10, FontStyle.Bold),
                FlatStyle = FlatStyle.Flat,
                Cursor = Cursors.Hand
            };
            btnLogin.FlatAppearance.BorderSize = 0;
            btnLogin.Click += (s, e) => HandleStandardLogin();

            // Divider
            Label lblOr = new Label
            {
                Text = "────────  OR  ────────",
                ForeColor = Color.FromArgb(150, 150, 150),
                Location = new Point(20, 248),
                Size = new Size(314, 20),
                TextAlign = ContentAlignment.MiddleCenter,
                Font = new Font("Segoe UI", 8.5f)
            };

            // Google Sign In Button
            Button btnGoogle = new Button
            {
                Text = "   G   Sign in with Google",
                Location = new Point(20, 276),
                Size = new Size(314, 42),
                BackColor = Color.White,
                ForeColor = Color.FromArgb(60, 64, 67),
                Font = new Font("Segoe UI", 10, FontStyle.Bold),
                FlatStyle = FlatStyle.Flat,
                Cursor = Cursors.Hand
            };
            btnGoogle.FlatAppearance.BorderColor = Color.FromArgb(218, 220, 224);
            btnGoogle.Click += (s, e) => ShowGoogleChooser();

            // Quick Demo hint
            Label lblHint = new Label
            {
                Text = "Demo Account: admin / admin123\nCompatible with Windows 7, 8, 10 & 11",
                ForeColor = Color.FromArgb(120, 120, 120),
                Location = new Point(20, 335),
                Size = new Size(314, 40),
                TextAlign = ContentAlignment.MiddleCenter,
                Font = new Font("Segoe UI", 8.5f)
            };

            body.Controls.Add(lblSignIn);
            body.Controls.Add(lblUser);
            body.Controls.Add(txtUsername);
            body.Controls.Add(lblPass);
            body.Controls.Add(txtPassword);
            body.Controls.Add(lblError);
            body.Controls.Add(btnLogin);
            body.Controls.Add(lblOr);
            body.Controls.Add(btnGoogle);
            body.Controls.Add(lblHint);

            loginPanel.Controls.Add(headerPanel);
            loginPanel.Controls.Add(body);

            this.Controls.Add(loginPanel);
        }

        private void HandleStandardLogin()
        {
            string u = txtUsername.Text.Trim();
            string p = txtPassword.Text.Trim();

            if (string.IsNullOrEmpty(u) || string.IsNullOrEmpty(p))
            {
                lblError.Text = "Please enter both username and password.";
                lblError.Visible = true;
                return;
            }

            ShowDashboard(u, u + "@company.com", false);
        }

        private void ShowGoogleChooser()
        {
            Form chooser = new Form
            {
                Text = "Sign in with Google",
                Size = new Size(380, 420),
                StartPosition = FormStartPosition.CenterParent,
                FormBorderStyle = FormBorderStyle.FixedDialog,
                MaximizeBox = false,
                MinimizeBox = false,
                BackColor = Color.White
            };

            Label title = new Label
            {
                Text = "Choose an account\nto continue to HCM Portal",
                Font = new Font("Segoe UI", 12, FontStyle.Regular),
                Location = new Point(24, 20),
                Size = new Size(320, 50)
            };

            Button acc1 = new Button
            {
                Text = "Hersin\nhersin51015@gmail.com",
                Location = new Point(24, 80),
                Size = new Size(316, 52),
                TextAlign = ContentAlignment.MiddleLeft,
                BackColor = Color.FromArgb(248, 249, 250),
                FlatStyle = FlatStyle.Flat,
                Font = new Font("Segoe UI", 9.5f),
                Cursor = Cursors.Hand
            };
            acc1.FlatAppearance.BorderColor = Color.FromArgb(220, 220, 220);
            acc1.Click += (s, e) =>
            {
                chooser.Close();
                ShowDashboard("Hersin", "hersin51015@gmail.com", true);
            };

            Button acc2 = new Button
            {
                Text = "Google User\ngoogle.user@gmail.com",
                Location = new Point(24, 142),
                Size = new Size(316, 52),
                TextAlign = ContentAlignment.MiddleLeft,
                BackColor = Color.FromArgb(248, 249, 250),
                FlatStyle = FlatStyle.Flat,
                Font = new Font("Segoe UI", 9.5f),
                Cursor = Cursors.Hand
            };
            acc2.FlatAppearance.BorderColor = Color.FromArgb(220, 220, 220);
            acc2.Click += (s, e) =>
            {
                chooser.Close();
                ShowDashboard("Google User", "google.user@gmail.com", true);
            };

            Button btnWeb = new Button
            {
                Text = "🌐 Open Live Google Web Sign-In",
                Location = new Point(24, 210),
                Size = new Size(316, 40),
                BackColor = Color.FromArgb(26, 115, 232),
                ForeColor = Color.White,
                FlatStyle = FlatStyle.Flat,
                Font = new Font("Segoe UI", 9, FontStyle.Bold),
                Cursor = Cursors.Hand
            };
            btnWeb.Click += (s, e) =>
            {
                try
                {
                    Process.Start("https://accounts.google.com/signin/v2/identifier");
                }
                catch { }
                chooser.Close();
                ShowDashboard("Hersin", "hersin51015@gmail.com", true);
            };

            chooser.Controls.Add(title);
            chooser.Controls.Add(acc1);
            chooser.Controls.Add(acc2);
            chooser.Controls.Add(btnWeb);

            chooser.ShowDialog(this);
        }

        private void ShowDashboard(string name, string email, bool isGoogle)
        {
            if (loginPanel != null) loginPanel.Visible = false;

            loggedInPanel = new Panel
            {
                Dock = DockStyle.Fill,
                BackColor = Color.FromArgb(248, 249, 250)
            };

            Panel header = new Panel
            {
                Dock = DockStyle.Top,
                Height = 90,
                BackColor = Color.FromArgb(26, 115, 232)
            };

            Label title = new Label
            {
                Text = "Welcome to HCM Workspace",
                ForeColor = Color.White,
                Font = new Font("Segoe UI", 14, FontStyle.Bold),
                Location = new Point(20, 18),
                AutoSize = true
            };

            Label sub = new Label
            {
                Text = "Logged in as " + name,
                ForeColor = Color.FromArgb(220, 235, 252),
                Font = new Font("Segoe UI", 9.5f),
                Location = new Point(20, 48),
                AutoSize = true
            };

            header.Controls.Add(title);
            header.Controls.Add(sub);

            Panel card = new Panel
            {
                Location = new Point(20, 110),
                Size = new Size(364, 460),
                BackColor = Color.White,
                BorderStyle = BorderStyle.FixedSingle
            };

            Label cardTitle = new Label
            {
                Text = "Active Session Details",
                Font = new Font("Segoe UI", 12, FontStyle.Bold),
                Location = new Point(16, 16),
                AutoSize = true
            };

            Label lblN = new Label
            {
                Text = "Name: " + name + (isGoogle ? " (Google Verified 🌐)" : ""),
                Font = new Font("Segoe UI", 10),
                Location = new Point(16, 56),
                Size = new Size(330, 24)
            };

            Label lblE = new Label
            {
                Text = "Email: " + email,
                Font = new Font("Segoe UI", 10),
                Location = new Point(16, 86),
                Size = new Size(330, 24)
            };

            Label lblStatus = new Label
            {
                Text = "Status: Active Session (Online)",
                ForeColor = Color.FromArgb(30, 142, 62),
                Font = new Font("Segoe UI", 10, FontStyle.Bold),
                Location = new Point(16, 116),
                Size = new Size(330, 24)
            };

            Button btnLogout = new Button
            {
                Text = "Sign Out",
                Location = new Point(16, 380),
                Size = new Size(330, 40),
                BackColor = Color.FromArgb(217, 48, 37),
                ForeColor = Color.White,
                Font = new Font("Segoe UI", 10, FontStyle.Bold),
                FlatStyle = FlatStyle.Flat,
                Cursor = Cursors.Hand
            };
            btnLogout.Click += (s, e) =>
            {
                this.Controls.Remove(loggedInPanel);
                loginPanel.Visible = true;
            };

            card.Controls.Add(cardTitle);
            card.Controls.Add(lblN);
            card.Controls.Add(lblE);
            card.Controls.Add(lblStatus);
            card.Controls.Add(btnLogout);

            loggedInPanel.Controls.Add(header);
            loggedInPanel.Controls.Add(card);

            this.Controls.Add(loggedInPanel);
        }
    }
}
