import { useState } from "react";
import {
  Button,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
} from "react-native";
import { AuthStackParamList } from "../navigation/AuthStackNavigator";
import { NativeStackScreenProps } from "@react-navigation/native-stack";

type Props = NativeStackScreenProps<AuthStackParamList, "RegisterScreen">;

const Register = ({ route, navigation }: Props) => {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

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
      ></TextInput>
      <TextInput
        style={styles.input}
        value={password}
        onChangeText={(text) => setPassword(text)}
        autoCorrect={false}
        placeholder="Password"
        secureTextEntry
      ></TextInput>
      <Pressable style={styles.button} onPress={() => console.log(username)}>
        <View>
          <Text style={styles.buttonText}>Register</Text>
        </View>
      </Pressable>
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
