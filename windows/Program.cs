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
                Size = new Size(420, 480),
                StartPosition = FormStartPosition.CenterParent,
                FormBorderStyle = FormBorderStyle.FixedDialog,
                MaximizeBox = false,
                MinimizeBox = false,
                BackColor = Color.White
            };

            Panel contentPanel = new Panel
            {
                Dock = DockStyle.Fill,
                BackColor = Color.White,
                Padding = new Padding(24)
            };

            string userEmail = "";
            string userDisplayName = "";

            void ShowEmailStep()
            {
                contentPanel.Controls.Clear();

                Label brand = new Label
                {
                    Text = "G  Sign in with Google",
                    Font = new Font("Segoe UI", 11.5f, FontStyle.Bold),
                    ForeColor = Color.FromArgb(60, 64, 67),
                    Location = new Point(24, 20),
                    Size = new Size(350, 26)
                };

                Label title = new Label
                {
                    Text = "Sign in",
                    Font = new Font("Segoe UI", 16, FontStyle.Regular),
                    Location = new Point(24, 60),
                    Size = new Size(350, 32)
                };

                Label sub = new Label
                {
                    Text = "to continue to HCM Workspace",
                    Font = new Font("Segoe UI", 9.5f),
                    ForeColor = Color.FromArgb(95, 99, 104),
                    Location = new Point(24, 94),
                    Size = new Size(350, 22)
                };

                TextBox txtEmail = new TextBox
                {
                    Location = new Point(24, 130),
                    Size = new Size(350, 32),
                    Font = new Font("Segoe UI", 11)
                };

                Label note = new Label
                {
                    Text = "To continue, Google will share your name, email address, and profile picture with HCM Workspace.",
                    Font = new Font("Segoe UI", 8.5f),
                    ForeColor = Color.FromArgb(95, 99, 104),
                    Location = new Point(24, 180),
                    Size = new Size(350, 44)
                };

                Button btnCancel = new Button
                {
                    Text = "Cancel",
                    Location = new Point(24, 250),
                    Size = new Size(100, 36),
                    FlatStyle = FlatStyle.Flat,
                    ForeColor = Color.FromArgb(26, 115, 232),
                    Font = new Font("Segoe UI", 9.5f, FontStyle.Bold),
                    Cursor = Cursors.Hand
                };
                btnCancel.FlatAppearance.BorderSize = 0;
                btnCancel.Click += (s, e) => chooser.Close();

                Button btnNext = new Button
                {
                    Text = "Next",
                    Location = new Point(274, 250),
                    Size = new Size(100, 36),
                    BackColor = Color.FromArgb(26, 115, 232),
                    ForeColor = Color.White,
                    FlatStyle = FlatStyle.Flat,
                    Font = new Font("Segoe UI", 9.5f, FontStyle.Bold),
                    Cursor = Cursors.Hand
                };
                btnNext.FlatAppearance.BorderSize = 0;
                btnNext.Click += (s, e) =>
                {
                    string input = txtEmail.Text.Trim();
                    if (!string.IsNullOrWhiteSpace(input))
                    {
                        userEmail = input.Contains("@") ? input : input + "@gmail.com";
                        userDisplayName = userEmail.Split('@')[0].Replace(".", " ");
                        ShowPasswordStep();
                    }
                };

                contentPanel.Controls.Add(brand);
                contentPanel.Controls.Add(title);
                contentPanel.Controls.Add(sub);
                contentPanel.Controls.Add(txtEmail);
                contentPanel.Controls.Add(note);
                contentPanel.Controls.Add(btnCancel);
                contentPanel.Controls.Add(btnNext);
            }

            void ShowPasswordStep()
            {
                contentPanel.Controls.Clear();

                Label title = new Label
                {
                    Text = "Welcome",
                    Font = new Font("Segoe UI", 16, FontStyle.Regular),
                    Location = new Point(24, 30),
                    Size = new Size(350, 32)
                };

                Label pill = new Label
                {
                    Text = "🔒 " + userEmail,
                    Font = new Font("Segoe UI", 9f, FontStyle.Bold),
                    ForeColor = Color.FromArgb(60, 64, 67),
                    BackColor = Color.FromArgb(241, 243, 244),
                    Location = new Point(24, 70),
                    Size = new Size(350, 26),
                    TextAlign = ContentAlignment.MiddleLeft
                };

                Label sub = new Label
                {
                    Text = "Enter your Google password:",
                    Font = new Font("Segoe UI", 9.5f),
                    ForeColor = Color.FromArgb(95, 99, 104),
                    Location = new Point(24, 115),
                    Size = new Size(350, 20)
                };

                TextBox txtPass = new TextBox
                {
                    Location = new Point(24, 140),
                    Size = new Size(350, 32),
                    Font = new Font("Segoe UI", 11),
                    PasswordChar = '●'
                };

                CheckBox chkShow = new CheckBox
                {
                    Text = "Show password",
                    Location = new Point(24, 180),
                    Size = new Size(200, 24),
                    Font = new Font("Segoe UI", 9)
                };
                chkShow.CheckedChanged += (s, e) => { txtPass.PasswordChar = chkShow.Checked ? '\0' : '●'; };

                Button btnBack = new Button
                {
                    Text = "Back",
                    Location = new Point(24, 240),
                    Size = new Size(90, 36),
                    FlatStyle = FlatStyle.Flat,
                    ForeColor = Color.FromArgb(26, 115, 232),
                    Font = new Font("Segoe UI", 9.5f, FontStyle.Bold),
                    Cursor = Cursors.Hand
                };
                btnBack.FlatAppearance.BorderSize = 0;
                btnBack.Click += (s, e) => ShowEmailStep();

                Button btnNext = new Button
                {
                    Text = "Next",
                    Location = new Point(274, 240),
                    Size = new Size(100, 36),
                    BackColor = Color.FromArgb(26, 115, 232),
                    ForeColor = Color.White,
                    FlatStyle = FlatStyle.Flat,
                    Font = new Font("Segoe UI", 9.5f, FontStyle.Bold),
                    Cursor = Cursors.Hand
                };
                btnNext.FlatAppearance.BorderSize = 0;
                btnNext.Click += (s, e) =>
                {
                    if (txtPass.Text.Length >= 4)
                    {
                        Show2FAStep();
                    }
                    else
                    {
                        MessageBox.Show("Please enter your account password.", "Google Sign-In", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    }
                };

                contentPanel.Controls.Add(title);
                contentPanel.Controls.Add(pill);
                contentPanel.Controls.Add(sub);
                contentPanel.Controls.Add(txtPass);
                contentPanel.Controls.Add(chkShow);
                contentPanel.Controls.Add(btnBack);
                contentPanel.Controls.Add(btnNext);
            }

            void Show2FAStep()
            {
                contentPanel.Controls.Clear();
                int promptNum = new Random().Next(10, 99);

                Label title = new Label
                {
                    Text = "2-Step Verification",
                    Font = new Font("Segoe UI", 15, FontStyle.Regular),
                    Location = new Point(24, 20),
                    Size = new Size(350, 30)
                };

                Label sub = new Label
                {
                    Text = "Google sent a notification to your phone. Tap Yes on the prompt, then match this number:",
                    Font = new Font("Segoe UI", 9.5f),
                    ForeColor = Color.FromArgb(95, 99, 104),
                    Location = new Point(24, 55),
                    Size = new Size(350, 42)
                };

                Label badge = new Label
                {
                    Text = promptNum.ToString(),
                    Font = new Font("Segoe UI", 28, FontStyle.Bold),
                    ForeColor = Color.FromArgb(26, 115, 232),
                    BackColor = Color.FromArgb(232, 240, 254),
                    TextAlign = ContentAlignment.MiddleCenter,
                    Location = new Point(140, 110),
                    Size = new Size(110, 65)
                };

                Button btnConfirm = new Button
                {
                    Text = "✓ I tapped Yes on my device",
                    Location = new Point(24, 200),
                    Size = new Size(350, 40),
                    BackColor = Color.FromArgb(26, 115, 232),
                    ForeColor = Color.White,
                    FlatStyle = FlatStyle.Flat,
                    Font = new Font("Segoe UI", 10, FontStyle.Bold),
                    Cursor = Cursors.Hand
                };
                btnConfirm.FlatAppearance.BorderSize = 0;
                btnConfirm.Click += (s, e) =>
                {
                    chooser.Close();
                    ShowDashboard(userDisplayName, userEmail, true);
                };

                Label altText = new Label
                {
                    Text = "Or enter 6-digit Authenticator / 8-digit backup code:",
                    Font = new Font("Segoe UI", 8.5f),
                    ForeColor = Color.FromArgb(95, 99, 104),
                    Location = new Point(24, 260),
                    Size = new Size(350, 20)
                };

                TextBox txtCode = new TextBox
                {
                    Location = new Point(24, 285),
                    Size = new Size(230, 30),
                    Font = new Font("Segoe UI", 11)
                };

                Button btnVerifyCode = new Button
                {
                    Text = "Verify Code",
                    Location = new Point(264, 283),
                    Size = new Size(110, 34),
                    BackColor = Color.FromArgb(241, 243, 244),
                    ForeColor = Color.FromArgb(60, 64, 67),
                    FlatStyle = FlatStyle.Flat,
                    Font = new Font("Segoe UI", 9, FontStyle.Bold),
                    Cursor = Cursors.Hand
                };
                btnVerifyCode.Click += (s, e) =>
                {
                    if (txtCode.Text.Trim().Length >= 6)
                    {
                        chooser.Close();
                        ShowDashboard(userDisplayName, userEmail, true);
                    }
                    else
                    {
                        MessageBox.Show("Please enter a valid 6-digit Authenticator code or 8-digit backup code.", "2-Step Verification", MessageBoxButtons.OK, MessageBoxIcon.Information);
                    }
                };

                contentPanel.Controls.Add(title);
                contentPanel.Controls.Add(sub);
                contentPanel.Controls.Add(badge);
                contentPanel.Controls.Add(btnConfirm);
                contentPanel.Controls.Add(altText);
                contentPanel.Controls.Add(txtCode);
                contentPanel.Controls.Add(btnVerifyCode);
            }

            ShowEmailStep();
            chooser.Controls.Add(contentPanel);
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
                Location = new Point(16, 340),
                Size = new Size(330, 40),
                BackColor = Color.FromArgb(32, 33, 36),
                ForeColor = Color.White,
                Font = new Font("Segoe UI", 10, FontStyle.Bold),
                FlatStyle = FlatStyle.Flat,
                Cursor = Cursors.Hand
            };
            btnLogout.FlatAppearance.BorderSize = 0;
            btnLogout.Click += (s, e) =>
            {
                this.Controls.Remove(loggedInPanel);
                loginPanel.Visible = true;
            };

            Button btnDeleteAccount = new Button
            {
                Text = "🗑 Delete Account",
                Location = new Point(16, 390),
                Size = new Size(330, 38),
                BackColor = Color.FromArgb(254, 242, 242),
                ForeColor = Color.FromArgb(220, 38, 38),
                Font = new Font("Segoe UI", 9.5f, FontStyle.Bold),
                FlatStyle = FlatStyle.Flat,
                Cursor = Cursors.Hand
            };
            btnDeleteAccount.FlatAppearance.BorderColor = Color.FromArgb(252, 165, 165);
            btnDeleteAccount.Click += (s, e) =>
            {
                var result = MessageBox.Show(
                    "Are you sure you want to permanently delete your account (" + name + ")?\n\nThis action cannot be undone.",
                    "Confirm Account Deletion",
                    MessageBoxButtons.YesNo,
                    MessageBoxIcon.Warning
                );

                if (result == DialogResult.Yes)
                {
                    this.Controls.Remove(loggedInPanel);
                    loginPanel.Visible = true;
                    txtUsername.Text = "";
                    txtPassword.Text = "";
                    lblError.Text = "Account (" + name + ") was permanently deleted.";
                    lblError.ForeColor = Color.FromArgb(30, 142, 62);
                    lblError.Visible = true;
                }
            };

            card.Controls.Add(cardTitle);
            card.Controls.Add(lblN);
            card.Controls.Add(lblE);
            card.Controls.Add(lblStatus);
            card.Controls.Add(btnLogout);
            card.Controls.Add(btnDeleteAccount);

            loggedInPanel.Controls.Add(header);
            loggedInPanel.Controls.Add(card);

            this.Controls.Add(loggedInPanel);
        }
    }
}
