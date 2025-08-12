/// The [AlarmModel] is used to display an alarm before the service starts.
class AlarmModel {
  /// The [title] is the title of the alarm.
  final String title;

  /// The [content] is the body text of the alarm.
  final String content;
  AlarmModel({required this.content, required this.title});

  Map<String, dynamic> toJson() => {"title": title, "content": content};
}
