import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Calendar from "../screens/Calendar";

type CalendarStackParamList = {
  CalendarScreen: undefined;
};

const Stack = createNativeStackNavigator<CalendarStackParamList>();

const CalendarStackNavigator = () => {
  return (
    <Stack.Navigator initialRouteName="CalendarScreen">
      <Stack.Screen name="CalendarScreen" component={Calendar}></Stack.Screen>
    </Stack.Navigator>
  );
};

export default CalendarStackNavigator;
