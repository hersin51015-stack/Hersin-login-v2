import SwiftUI

// MARK: - Models
struct UserAccount: Identifiable, Equatable {
    let id = UUID()
    var username: String
    var password: String
    var email: String
    var displayName: String
    var avatarEmoji: String = "👤"
    var isGoogleUser: Bool = false
}

enum AuthStatus: Equatable {
    case idle
    case success(message: String)
    case error(message: String)
}

// MARK: - Main App View
struct ContentView: View {
    @StateObject private var authViewModel = AuthViewModel()
    @State private var showingCreateAccountSheet = false

    var body: some View {
        ZStack {
            Color.black.ignoresSafeArea()

            if let currentUser = authViewModel.currentUser {
                LoggedInView(
                    user: currentUser,
                    onSignOut: {
                        authViewModel.signOut()
                    }
                )
                .transition(.asymmetric(insertion: .scale.combined(with: .opacity), removal: .opacity))
            } else {
                LoginView(
                    viewModel: authViewModel,
                    onOpenCreateAccount: {
                        showingCreateAccountSheet = true
                    }
                )
                .transition(.opacity)
            }
        }
        .animation(.spring(response: 0.45, dampingFraction: 0.75), value: authViewModel.currentUser)
        .sheet(isPresented: $showingCreateAccountSheet) {
            CreateAccountView(
                defaultEmail: "user@gmail.com",
                onAccountCreated: { newAccount in
                    authViewModel.registerAccount(newAccount)
                    showingCreateAccountSheet = false
                },
                onCancel: {
                    showingCreateAccountSheet = false
                }
            )
            .preferredColorScheme(.dark)
        }
    }
}

// MARK: - View Model
class AuthViewModel: ObservableObject {
    @Published var accounts: [UserAccount] = [
        UserAccount(username: "admin", password: "password123", email: "admin@example.com", displayName: "Administrator", avatarEmoji: "🛡️"),
        UserAccount(username: "user", password: "password", email: "user@example.com", displayName: "Member", avatarEmoji: "✨")
    ]
    
    @Published var currentUser: UserAccount? = nil
    @Published var authStatus: AuthStatus = .idle

    func login(usernameInput: String, passwordInput: String) {
        let trimmedUser = usernameInput.trimmingCharacters(in: .whitespacesAndNewlines)
        let trimmedPass = passwordInput.trimmingCharacters(in: .whitespacesAndNewlines)

        guard !trimmedUser.isEmpty && !trimmedPass.isEmpty else {
            authStatus = .error(message: "Wrong! Please enter both username and password.")
            return
        }

        if let found = accounts.first(where: {
            $0.username.caseInsensitiveCompare(trimmedUser) == .orderedSame && $0.password == trimmedPass
        }) {
            currentUser = found
            authStatus = .success(message: "Right! Login successful. Welcome back!")
        } else {
            authStatus = .error(message: "Wrong! Invalid username or password.")
        }
    }

    func registerAccount(_ account: UserAccount) {
        accounts.removeAll(where: { $0.username.caseInsensitiveCompare(account.username) == .orderedSame })
        accounts.append(account)
        currentUser = account
        authStatus = .success(message: account.isGoogleUser ? "Right! Signed in with Google as \(account.email)" : "Right! Account created successfully.")
    }

    func signOut() {
        currentUser = nil
        authStatus = .idle
    }

    func clearStatus() {
        authStatus = .idle
    }
}

// MARK: - Login View (Faithful Untitled Design)
struct LoginView: View {
    @ObservedObject var viewModel: AuthViewModel
    var onOpenCreateAccount: () -> Void

    @State private var username = ""
    @State private var password = ""
    @State private var isPasswordVisible = false
    @State private var shakeOffset: CGFloat = 0

    var body: some View {
        ScrollView {
            VStack(spacing: 24) {
                Spacer().frame(height: 20)

                // Horned H Logo Emblem
                HornedHLogoView()
                    .frame(width: 140, height: 140)

                Text("H E R S I N")
                    .font(.system(size: 26, weight: .black, design: .monospaced))
                    .foregroundColor(.white)
                    .tracking(6)

                Text("SECURE ACCESS PORTAL")
                    .font(.system(size: 11, weight: .bold, design: .monospaced))
                    .foregroundColor(Color.cyan.opacity(0.85))
                    .tracking(3)

                // Feedback banner
                if case .error(let msg) = viewModel.authStatus {
                    HStack(spacing: 10) {
                        Image(systemName: "exclamationmark.triangle.fill")
                            .foregroundColor(.white)
                        Text(msg)
                            .font(.system(size: 13, weight: .bold))
                            .foregroundColor(.white)
                    }
                    .padding(.horizontal, 16)
                    .padding(.vertical, 10)
                    .frame(maxWidth: 360)
                    .background(Color.red.opacity(0.9))
                    .cornerRadius(6)
                    .overlay(RoundedRectangle(cornerRadius: 6).stroke(Color.white, lineWidth: 1))
                }

                // Shakeable Input Fields
                VStack(spacing: 16) {
                    // Username input
                    ZStack(alignment: .leading) {
                        Rectangle()
                            .fill(Color.white)
                            .frame(height: 52)

                        if username.isEmpty {
                            Text("Enter username")
                                .foregroundColor(Color(red: 0.12, green: 0.16, blue: 0.23))
                                .font(.system(size: 18))
                                .padding(.horizontal, 16)
                        }

                        TextField("", text: $username)
                            .font(.system(size: 18, weight: .medium))
                            .foregroundColor(.black)
                            .padding(.horizontal, 16)
                            .autocapitalization(.none)
                            .disableAutocorrection(true)
                    }
                    .frame(maxWidth: 360)

                    // Password input
                    ZStack(alignment: .trailing) {
                        Rectangle()
                            .fill(Color.white)
                            .frame(height: 52)

                        if password.isEmpty {
                            HStack {
                                Text("Enter password")
                                    .foregroundColor(Color(red: 0.12, green: 0.16, blue: 0.23))
                                    .font(.system(size: 18))
                                    .padding(.leading, 16)
                                Spacer()
                            }
                        }

                        HStack {
                            if isPasswordVisible {
                                TextField("", text: $password)
                                    .font(.system(size: 18, weight: .medium))
                                    .foregroundColor(.black)
                            } else {
                                SecureField("", text: $password)
                                    .font(.system(size: 18, weight: .medium))
                                    .foregroundColor(.black)
                            }
                            
                            Button(action: { isPasswordVisible.toggle() }) {
                                Image(systemName: isPasswordVisible ? "eye.slash.fill" : "eye.fill")
                                    .foregroundColor(Color.black.opacity(0.6))
                                    .padding(.trailing, 14)
                            }
                        }
                        .padding(.leading, 16)
                    }
                    .frame(maxWidth: 360)

                    // Login Button (Cyan / Bright High-Contrast)
                    Button(action: {
                        viewModel.login(usernameInput: username, passwordInput: password)
                        if case .error = viewModel.authStatus {
                            triggerShake()
                        }
                    }) {
                        Text("LOGIN")
                            .font(.system(size: 17, weight: .black))
                            .tracking(2)
                            .foregroundColor(.black)
                            .frame(maxWidth: .infinity)
                            .frame(height: 52)
                            .background(Color.cyan)
                    }
                    .frame(maxWidth: 360)
                }
                .offset(x: shakeOffset)

                // Divider
                HStack {
                    Rectangle().fill(Color.white.opacity(0.2)).frame(height: 1)
                    Text("OR").font(.system(size: 12, weight: .bold)).foregroundColor(.gray)
                    Rectangle().fill(Color.white.opacity(0.2)).frame(height: 1)
                }
                .frame(maxWidth: 360)
                .padding(.vertical, 4)

                // Google Sign In Button
                Button(action: {
                    let googleUser = UserAccount(
                        username: "google_user",
                        password: "google_oauth_pass",
                        email: "user@gmail.com",
                        displayName: "Google User",
                        avatarEmoji: "🌐",
                        isGoogleUser: true
                    )
                    viewModel.registerAccount(googleUser)
                }) {
                    HStack(spacing: 12) {
                        Text("G")
                            .font(.system(size: 20, weight: .bold, design: .serif))
                            .foregroundColor(.white)
                        Text("Continue with Google")
                            .font(.system(size: 15, weight: .bold))
                            .foregroundColor(.white)
                    }
                    .frame(maxWidth: .infinity)
                    .frame(height: 48)
                    .background(Color(red: 0.15, green: 0.15, blue: 0.2))
                    .cornerRadius(8)
                    .overlay(RoundedRectangle(cornerRadius: 8).stroke(Color.white.opacity(0.2), lineWidth: 1))
                }
                .frame(maxWidth: 360)

                // Create Account Button
                Button(action: onOpenCreateAccount) {
                    Text("Don't have an account? CREATE ACCOUNT")
                        .font(.system(size: 13, weight: .bold))
                        .foregroundColor(Color.cyan)
                }
                .padding(.top, 8)

                // Preset Accounts Shortcut Bar
                VStack(alignment: .leading, spacing: 8) {
                    Text("QUICK TEST ACCOUNTS:")
                        .font(.system(size: 10, weight: .bold, design: .monospaced))
                        .foregroundColor(.gray)

                    HStack(spacing: 8) {
                        ForEach(viewModel.accounts) { acc in
                            Button(action: {
                                username = acc.username
                                password = acc.password
                            }) {
                                Text("\(acc.avatarEmoji) \(acc.username)")
                                    .font(.system(size: 12, weight: .semibold))
                                    .foregroundColor(.white)
                                    .padding(.horizontal, 10)
                                    .padding(.vertical, 6)
                                    .background(Color.white.opacity(0.1))
                                    .cornerRadius(6)
                            }
                        }
                    }
                }
                .padding(.top, 16)

                Spacer()
            }
            .padding(.horizontal, 24)
        }
    }

    private func triggerShake() {
        withAnimation(.default) { shakeOffset = -15 }
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.08) {
            withAnimation(.default) { shakeOffset = 15 }
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.16) {
            withAnimation(.default) { shakeOffset = -8 }
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.24) {
            withAnimation(.default) { shakeOffset = 0 }
        }
    }
}

// MARK: - Horned H Vector View
struct HornedHLogoView: View {
    var body: some View {
        ZStack {
            // Glow Circle
            Circle()
                .fill(
                    RadialGradient(
                        colors: [Color.cyan.opacity(0.4), Color.clear],
                        center: .center,
                        startRadius: 10,
                        endRadius: 70
                    )
                )

            // Stylized Horned H Artwork
            VStack(spacing: -6) {
                // Horns
                HStack(spacing: 24) {
                    Image(systemName: "flame.fill")
                        .rotationEffect(.degrees(-35))
                        .font(.system(size: 26))
                        .foregroundColor(Color.cyan)

                    Image(systemName: "flame.fill")
                        .rotationEffect(.degrees(35))
                        .font(.system(size: 26))
                        .foregroundColor(Color.cyan)
                }

                // Central 'H'
                Text("H")
                    .font(.system(size: 78, weight: .black, design: .serif))
                    .foregroundStyle(
                        LinearGradient(
                            colors: [Color.white, Color.cyan],
                            startPoint: .top,
                            endPoint: .bottom
                        )
                    )
                    .shadow(color: Color.cyan, radius: 10)
            }
        }
    }
}

// MARK: - Logged In Screen
struct LoggedInView: View {
    var user: UserAccount
    var onSignOut: () -> Void

    var body: some View {
        VStack(spacing: 28) {
            Spacer()

            // Success Badge
            ZStack {
                Circle()
                    .fill(Color.green.opacity(0.2))
                    .frame(width: 120, height: 120)

                Image(systemName: "checkmark.shield.fill")
                    .font(.system(size: 64))
                    .foregroundColor(Color(red: 0.2, green: 0.9, blue: 0.5))
            }

            VStack(spacing: 8) {
                Text("Right!")
                    .font(.system(size: 38, weight: .black))
                    .foregroundColor(Color(red: 0.2, green: 0.9, blue: 0.5))

                Text("AUTHENTICATED ACCESS")
                    .font(.system(size: 13, weight: .bold, design: .monospaced))
                    .foregroundColor(.white.opacity(0.8))
                    .tracking(3)
            }

            // User Info Card
            VStack(spacing: 12) {
                Text(user.avatarEmoji)
                    .font(.system(size: 48))

                Text(user.displayName)
                    .font(.system(size: 22, weight: .bold))
                    .foregroundColor(.white)

                Text("@\(user.username)")
                    .font(.system(size: 15, weight: .medium, design: .monospaced))
                    .foregroundColor(Color.cyan)

                Text(user.email)
                    .font(.system(size: 13))
                    .foregroundColor(.gray)
            }
            .frame(maxWidth: 340)
            .padding(.vertical, 24)
            .background(Color(red: 0.1, green: 0.1, blue: 0.15))
            .cornerRadius(16)
            .overlay(RoundedRectangle(cornerRadius: 16).stroke(Color.white.opacity(0.15), lineWidth: 1))

            Spacer()

            // Sign Out Button
            Button(action: onSignOut) {
                HStack {
                    Image(systemName: "rectangle.portrait.and.arrow.right")
                    Text("SIGN OUT")
                        .font(.system(size: 16, weight: .bold))
                }
                .foregroundColor(.white)
                .frame(maxWidth: 340)
                .frame(height: 52)
                .background(Color.red.opacity(0.85))
                .cornerRadius(10)
            }
            .padding(.bottom, 32)
        }
        .padding(.horizontal, 24)
    }
}

// MARK: - Create Account Modal
struct CreateAccountView: View {
    var defaultEmail: String
    var onAccountCreated: (UserAccount) -> Void
    var onCancel: () -> Void

    @State private var username = ""
    @State private var password = ""
    @State private var email = ""
    @State private var displayName = ""
    @State private var errorMessage = ""

    var body: some View {
        NavigationView {
            ZStack {
                Color.black.ignoresSafeArea()

                ScrollView {
                    VStack(spacing: 18) {
                        Text("Create New Account")
                            .font(.system(size: 22, weight: .bold))
                            .foregroundColor(.white)
                            .padding(.top, 12)

                        if !errorMessage.isEmpty {
                            Text(errorMessage)
                                .font(.system(size: 13, weight: .bold))
                                .foregroundColor(.red)
                        }

                        // Form Inputs
                        CustomInputField(title: "Username", placeholder: "e.g. jdoe", text: $username)
                        CustomInputField(title: "Display Name", placeholder: "e.g. John Doe", text: $displayName)
                        CustomInputField(title: "Email", placeholder: "e.g. name@mail.com", text: $email)
                        CustomInputField(title: "Password", placeholder: "Create strong password", text: $password, isSecure: true)

                        Button(action: {
                            guard !username.trimmingCharacters(in: .whitespaces).isEmpty,
                                  !password.trimmingCharacters(in: .whitespaces).isEmpty else {
                                errorMessage = "Username and password cannot be empty."
                                return
                            }

                            let newAcc = UserAccount(
                                username: username.trimmingCharacters(in: .whitespaces),
                                password: password.trimmingCharacters(in: .whitespaces),
                                email: email.isEmpty ? defaultEmail : email,
                                displayName: displayName.isEmpty ? username : displayName,
                                avatarEmoji: "⚡"
                            )
                            onAccountCreated(newAcc)
                        }) {
                            Text("REGISTER ACCOUNT")
                                .font(.system(size: 16, weight: .bold))
                                .foregroundColor(.black)
                                .frame(maxWidth: .infinity)
                                .frame(height: 50)
                                .background(Color.cyan)
                                .cornerRadius(8)
                        }
                        .padding(.top, 10)
                    }
                    .padding(24)
                }
            }
            .navigationBarItems(
                leading: Button("Cancel", action: onCancel).foregroundColor(.gray)
            )
        }
    }
}

struct CustomInputField: View {
    var title: String
    var placeholder: String
    @Binding var text: String
    var isSecure: Bool = false

    var body: some View {
        VStack(alignment: .leading, spacing: 6) {
            Text(title)
                .font(.system(size: 12, weight: .bold, design: .monospaced))
                .foregroundColor(Color.cyan)

            if isSecure {
                SecureField(placeholder, text: $text)
                    .padding()
                    .background(Color.white.opacity(0.1))
                    .cornerRadius(8)
                    .foregroundColor(.white)
            } else {
                TextField(placeholder, text: $text)
                    .padding()
                    .background(Color.white.opacity(0.1))
                    .cornerRadius(8)
                    .foregroundColor(.white)
                    .autocapitalization(.none)
            }
        }
    }
}
