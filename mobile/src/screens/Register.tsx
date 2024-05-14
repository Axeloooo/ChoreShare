import { useEffect, useState } from "react";
import {
  ActivityIndicator,
  Alert,
  Button,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
} from "react-native";
import { AuthStackParamList } from "../navigation/AuthStackNavigator";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { usePostRegisterMutation } from "../redux/services/authService";
import { DecodedToken, jwtDecode } from "../utils/jwt";
import { useAppDispatch } from "../hooks/store";
import { setAuth } from "../redux/slices/authSlice";
import { insertSession } from "../database";

type Props = NativeStackScreenProps<AuthStackParamList, "RegisterScreen">;

const Register = ({ route, navigation }: Props) => {
  const [firstName, setFirstName] = useState<string>("");
  const [lastName, setLastName] = useState<string>("");
  const [email, setEmail] = useState<string>("");
  const [phone, setPhone] = useState<string>("");
  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");

  const dispatch = useAppDispatch();

  const [postRegister, response] = usePostRegisterMutation();

  useEffect(() => {
    if (response.isSuccess && response.data?.token) {
      try {
        const decodedToken: DecodedToken = jwtDecode(response.data.token);
        dispatch(
          setAuth({ userId: decodedToken.sub, token: response.data.token })
        );
        insertSession(decodedToken.sub, response.data.token);
      } catch (error) {
        console.error("Failed to decode token:", error);
      }
    } else if (response.isError) {
      console.error(response.error);
      Alert.alert("Wrong Credentials", "Please try again.");
    }
  }, [response, dispatch]);

  const handleRegister = async () => {
    postRegister({
      firstName: firstName,
      lastName: lastName,
      email: email,
      phone: phone,
      username: username,
      password: password,
    });
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>ChoreShare</Text>
      <Text style={styles.text}>Don't have an account yet? Register here!</Text>
      <TextInput
        style={styles.input}
        value={firstName}
        onChangeText={(text) => setFirstName(text)}
        placeholder="First Name"
        autoCorrect={false}
      ></TextInput>
      <TextInput
        style={styles.input}
        value={lastName}
        onChangeText={(text) => setLastName(text)}
        placeholder="Last Name"
        autoCorrect={false}
      ></TextInput>
      <TextInput
        style={styles.input}
        value={email}
        onChangeText={(text) => setEmail(text)}
        placeholder="Email"
        autoCorrect={false}
        autoCapitalize="none"
      ></TextInput>
      <TextInput
        style={styles.input}
        value={phone}
        onChangeText={(text) => setPhone(text)}
        placeholder="Phone Number"
        autoCorrect={false}
      ></TextInput>
      <TextInput
        style={styles.input}
        value={username}
        onChangeText={(text) => setUsername(text)}
        placeholder="Username"
        autoCorrect={false}
        autoCapitalize="none"
      ></TextInput>
      <TextInput
        style={styles.input}
        value={password}
        onChangeText={(text) => setPassword(text)}
        autoCorrect={false}
        placeholder="Password"
        autoCapitalize="none"
        secureTextEntry
      ></TextInput>
      {response.isLoading ? (
        <ActivityIndicator size="small" color="#3498ff"></ActivityIndicator>
      ) : (
        <Pressable style={styles.button} onPress={handleRegister}>
          <View>
            <Text style={styles.buttonText}>Register</Text>
          </View>
        </Pressable>
      )}
      <Button
        title="Login"
        onPress={() => navigation.navigate("LoginScreen")}
      ></Button>
    </View>
  );
};

export default Register;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  title: {
    fontSize: 30,
    fontWeight: "bold",
    margin: 12,
  },
  text: {
    fontSize: 16,
    margin: 12,
  },
  input: {
    width: "80%",
    height: 40,
    margin: 12,
    borderWidth: 1,
    padding: 10,
    borderRadius: 5,
    borderColor: "#ccc",
    backgroundColor: "#f8f8f8",
  },
  button: {
    width: "80%",
    height: 40,
    margin: 12,
    backgroundColor: "#3498ff",
    borderRadius: 5,
    justifyContent: "center",
    alignItems: "center",
  },
  buttonText: {
    color: "#f8f8f8",
    fontSize: 16,
  },
});
