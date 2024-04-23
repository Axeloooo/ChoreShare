import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Profile from "../screens/Profile";

type ProfileStackParamList = {
  ProfileScreen: undefined;
};

const Stack = createNativeStackNavigator<ProfileStackParamList>();

const ProfileStackNavigator = () => {
  return (
    <Stack.Navigator initialRouteName="ProfileScreen">
      <Stack.Screen name="ProfileScreen" component={Profile}></Stack.Screen>
    </Stack.Navigator>
  );
};

export default ProfileStackNavigator;
