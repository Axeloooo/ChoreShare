import {
  StyleSheet,
  TextInput,
  View,
  Button,
  Modal,
  TouchableOpacity,
  Text,
  Pressable,
  Alert,
  ActivityIndicator,
} from "react-native";
import { Picker } from "@react-native-picker/picker";
import { useEffect, useState } from "react";
import { NativeStackScreenProps } from "@react-navigation/native-stack";
import { ChoresStackParamList } from "../navigation/ChoresStackNavigator";
import { Auth } from "../redux/slices/authSlice";
import { useAppSelector } from "../hooks/store";

type Props = NativeStackScreenProps<ChoresStackParamList, "ChoresCreateScreen">;

const CreateChore = ({ route, navigation }: Props): React.JSX.Element => {
  const [title, setTitle] = useState<string>("");
  const [description, setDescription] = useState<string>("");
  const [status, setStatus] = useState<string>("");
  const [frequency, setFrequency] = useState<string>("");
  const [tag, setTag] = useState<string>("");

  const [isStatusPickerVisible, setStatusPickerVisible] =
    useState<boolean>(false);
  const [isFrequencyPickerVisible, setFrequencyPickerVisible] =
    useState<boolean>(false);
  const [isTagPickerVisible, setTagPickerVisible] = useState<boolean>(false);

  const [isLoading, setIsLoading] = useState<boolean>(false);

  const auth: Auth = useAppSelector((state) => state.auth);

  const handleCreateChore = async () => {
    setIsLoading(true);
    try {
      const response: Response = await fetch(
        "http://localhost:8888/api/v1/task",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${auth.token}`,
          },
          body: JSON.stringify({
            title: title,
            description: description,
            status: status,
            frequency: frequency,
            tag: tag,
            userId: auth.userId,
            householdId: "1",
          }),
        }
      );
      if (response.status !== 201) {
        const data = await response.json();
        Alert.alert("Error", data.message);
      }
      setIsLoading(false);
      navigation.goBack();
    } catch (error) {
      Alert.alert("Oops", "Something went wrong");
      setIsLoading(false);
      navigation.goBack();
    }
  };

  useEffect(() => {
    setTitle("");
    setDescription("");
    setStatus("");
    setFrequency("");
    setTag("");
  }, []);

  return (
    <View style={styles.container}>
      <TextInput
        placeholder="Title"
        value={title}
        onChangeText={(text) => setTitle(text)}
        style={styles.input}
      />
      <TextInput
        placeholder="Description"
        value={description}
        onChangeText={(text) => setDescription(text)}
        style={styles.input}
      />
      <TouchableOpacity
        onPress={() => setStatusPickerVisible(true)}
        style={styles.pickerButton}
      >
        <Text>{status ? status : "Select Status"}</Text>
      </TouchableOpacity>
      <Modal visible={isStatusPickerVisible} transparent={true}>
        <View style={styles.modalContainer}>
          <View style={styles.pickerContainer}>
            <Picker
              selectedValue={status}
              onValueChange={(itemValue) => {
                setStatus(itemValue);
                setStatusPickerVisible(false);
              }}
            >
              <Picker.Item label="Select Status" value="none" />
              <Picker.Item label="Pending" value="PENDING" />
              <Picker.Item label="In Progress" value="IN_PROGRESS" />
              <Picker.Item label="Completed" value="COMPLETED" />
            </Picker>
            <Button
              title="Close"
              onPress={() => setStatusPickerVisible(false)}
            />
          </View>
        </View>
      </Modal>
      <TouchableOpacity
        onPress={() => setFrequencyPickerVisible(true)}
        style={styles.pickerButton}
      >
        <Text>{frequency ? frequency : "Select Frequency"}</Text>
      </TouchableOpacity>
      <Modal visible={isFrequencyPickerVisible} transparent={true}>
        <View style={styles.modalContainer}>
          <View style={styles.pickerContainer}>
            <Picker
              selectedValue={frequency}
              onValueChange={(itemValue) => {
                setFrequency(itemValue);
                setFrequencyPickerVisible(false);
              }}
            >
              <Picker.Item label="Select Frequency" value="none" />
              <Picker.Item label="Once a Week" value="ONCE_A_WEEK" />
              <Picker.Item label="Twice a Week" value="TWICE_A_WEEK" />
              <Picker.Item label="Everyday" value="EVERYDAY" />
              <Picker.Item label="Every Other Day" value="EVERY_OTHER_DAY" />
            </Picker>
            <Button
              title="Close"
              onPress={() => setFrequencyPickerVisible(false)}
            />
          </View>
        </View>
      </Modal>
      <TouchableOpacity
        onPress={() => setTagPickerVisible(true)}
        style={styles.pickerButton}
      >
        <Text>{tag ? tag : "Select Tag"}</Text>
      </TouchableOpacity>
      <Modal visible={isTagPickerVisible} transparent={true}>
        <View style={styles.modalContainer}>
          <View style={styles.pickerContainer}>
            <Picker
              selectedValue={tag}
              onValueChange={(itemValue) => {
                setTag(itemValue);
                setTagPickerVisible(false);
              }}
            >
              <Picker.Item label="Select Tag" value="none" />
              <Picker.Item label="Kitchen" value="KITCHEN" />
              <Picker.Item label="Bathroom" value="BATHROOM" />
              <Picker.Item label="Living Room" value="LIVING_ROOM" />
              <Picker.Item label="General" value="GENERAL" />
            </Picker>
            <Button title="Close" onPress={() => setTagPickerVisible(false)} />
          </View>
        </View>
      </Modal>
      {isLoading ? (
        <ActivityIndicator size="small" color="#2f95dc" />
      ) : (
        <Pressable style={styles.button} onPress={handleCreateChore}>
          <Text style={styles.buttonText}>Login</Text>
        </Pressable>
      )}
    </View>
  );
};

export default CreateChore;

const styles = StyleSheet.create({
  container: {
    padding: 20,
    justifyContent: "center",
    alignItems: "center",
  },
  input: {
    height: 40,
    margin: 12,
    borderWidth: 1,
    padding: 10,
    borderRadius: 5,
    borderColor: "#ccc",
    backgroundColor: "#f8f8f8",
    width: "80%",
  },
  pickerButton: {
    height: 40,
    margin: 12,
    borderWidth: 1,
    padding: 10,
    borderRadius: 5,
    borderColor: "#ccc",
    backgroundColor: "#f8f8f8",
    width: "80%",
  },
  modalContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "rgba(0,0,0,0.5)",
    color: "#f8f8f8",
  },
  pickerContainer: {
    backgroundColor: "white",
    padding: 16,
    borderRadius: 8,
    width: "80%",
  },
  button: {
    width: "80%",
    height: 40,
    margin: 12,
    backgroundColor: "#2f95dc",
    borderRadius: 5,
    justifyContent: "center",
    alignItems: "center",
  },
  buttonText: {
    color: "#f8f8f8",
    fontSize: 16,
  },
});
