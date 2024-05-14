import {
  Pressable,
  StyleSheet,
  TextInput,
  View,
  Text,
  Button,
  Alert,
  ActivityIndicator,
} from "react-native";
import { useEffect, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { AuthStackParamList } from "../navigation/AuthStackNavigator";
import { usePostLoginMutation } from "../redux/services/authService";
import { DecodedToken, jwtDecode } from "../utils/jwt";
import { useAppDispatch } from "../hooks/store";
import { setAuth } from "../redux/slices/authSlice";
import { insertSession } from "../database";

type Props = NativeStackScreenProps<AuthStackParamList, "LoginScreen">;

const Login = ({ route, navigation }: Props) => {
  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");

  const dispatch = useAppDispatch();

  const [postLogin, response] = usePostLoginMutation();

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

  const handleLogin = () => {
    postLogin({ username: username, password: password });
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>ChoreShare</Text>
      <Text style={styles.text}>Already have an account? Login here!</Text>
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
        <ActivityIndicator size="small" color="#3498ff" />
      ) : (
        <Pressable style={styles.button} onPress={handleLogin}>
          <Text style={styles.buttonText}>Login</Text>
        </Pressable>
      )}
      <Button
        title="Register"
        onPress={() => navigation.navigate("RegisterScreen")}
      ></Button>
    </View>
  );
};

export default Login;

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
