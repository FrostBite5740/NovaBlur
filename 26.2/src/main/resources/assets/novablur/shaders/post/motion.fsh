#version 330

uniform sampler2D InSampler;
uniform sampler2D PrevSampler;

layout(std140) uniform MotionConfig {
    float Strength;
};

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 current = texture(InSampler, texCoord);
    vec4 previous = texture(PrevSampler, texCoord);
    // first frame after turning on: nothing to blend with yet
    float s = previous.a > 0.5 ? Strength : 0.0;
    fragColor = vec4(mix(current.rgb, previous.rgb, s), 1.0);
}
